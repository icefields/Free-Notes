/**
 * Copyright (C) 2024  Antonio Tari
 *
 * This file is a part of Libre Notes
 * Android self-hosting, note-taking, client + server application
 * @author Antonio Tari
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package luci.sixsixsix.homemessageshare.presentation.main

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import luci.sixsixsix.homemessageshare.common.Constants
import luci.sixsixsix.homemessageshare.common.Resource
import luci.sixsixsix.homemessageshare.di.WeakContext
import luci.sixsixsix.homemessageshare.domain.CollectionsRepository
import luci.sixsixsix.homemessageshare.domain.SettingsRepository
import luci.sixsixsix.homemessageshare.domain.models.Message
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection
import luci.sixsixsix.homemessageshare.domain.use_case.DeleteMessageUseCase
import luci.sixsixsix.homemessageshare.domain.use_case.EditMessageUseCase
import luci.sixsixsix.homemessageshare.domain.use_case.GetMessagesUseCase
import luci.sixsixsix.homemessageshare.domain.use_case.SyncNotesUseCase
import luci.sixsixsix.homemessageshare.domain.use_case.WriteNewMessageUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val weakContext: WeakContext,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val writeNewMessageUseCase: WriteNewMessageUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val editMessageUseCase: EditMessageUseCase,
    private val syncNotesUseCase: SyncNotesUseCase,
    private val settingsRepository: SettingsRepository,
    private val collectionsRepository: CollectionsRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    var state by savedStateHandle.saveable { mutableStateOf(MainState()) }
        private set

    private var getMessagesJob: Job? = null

    init {
        // listen to changes to the current notes collection configuration
        viewModelScope.launch {
            settingsRepository.settingsFlow.collectLatest {
                println("MainViewModel - new settings aaaa ${it.collectionName}")
                state = state.copy(notesCollection = it)
                // get all the messages every time collection changes
                getMessages()
            }
        }

        // listen to new collections
        viewModelScope.launch {
            collectionsRepository.collectionsLiveData.asFlow().collect { collections ->
                println("MainViewModel - new collections aaaa ${collections.size}")
                if (collections.isNotEmpty()) {
                    state = state.copy(collections = collections)
                } else {
                    // initialize with offline db
                    collectionsRepository.initializeOfflineUser().collect { resource ->
                        println("MainViewModel - new collections collecting")
                        when(resource) {
                            is Resource.Success -> resource.data?.let {
                                println("MainViewModel - new collections Success")

                                getMessages()
                            }
                            is Resource.Error -> weakContext.get()?.let { context -> }
                            is Resource.Loading -> { }
                        }
                    }
                }
            }
        }
    }

    fun onNewConfiguration(server: String, collectionName: String) = viewModelScope.launch {
        println("onNewConfiguration $collectionName $server aaaa")
        val updatedNotesCollection = state.notesCollection.copy(serverAddress = server, collectionName = collectionName)
        writeNewNotesConfiguration(updatedNotesCollection)
    }

    private fun writeNewNotesConfiguration(updatedNotesCollection: NotesCollection) = viewModelScope.launch {
        // update internal db
        collectionsRepository.writeCollection(updatedNotesCollection).collect { resource ->
            when(resource) {
                is Resource.Success ->
                    resource.data?.let { collections ->
                        state = state.copy(collections = collections)
                        // update current settings id in shared preferences
                        settingsRepository.writeCurrentNotesCollectionId(updatedNotesCollection.serverAddress, updatedNotesCollection.collectionName)
                    }
                is Resource.Error -> weakContext.get()?.let { context -> {
                    Toast.makeText(context, "Something went wrong,please try again ${resource.message}", Toast.LENGTH_LONG).show()
                    println("MainViewModel onNewConfiguration aaaa ${resource.message}")
                }
                }
                is Resource.Loading -> { }
            }
        }
    }

    fun toggleMaterialYou(enable: Boolean) {
        val updatedNotesCollection = state.notesCollection.copy(
            appTheme = if (enable) {
                Constants.THEME_DARK_YOU
            } else {
                Constants.THEME_DARK
            }
        )
        writeNewNotesConfiguration(updatedNotesCollection)
    }

    fun deleteCollection(collection: NotesCollection) = viewModelScope.launch {
        collectionsRepository.deleteCollection(collection).collect{ }
    }

    private fun parseMessagesResponse(resource: Resource<List<Message>>) {
        when(resource) {
            is Resource.Success ->
                resource.data?.let { messages ->
                    state = state.copy(messages = messages.reversed())
                }
            is Resource.Error -> weakContext.get()?.let { context -> {
                Toast.makeText(context, "Something went wrong,please try again ${resource.message}", Toast.LENGTH_LONG).show()
                println("MainViewModel getMessages parseMessagesResponse aaaa ${resource.message}")
            }
            }
            is Resource.Loading ->
                state = state.copy(loading = resource.isLoading)
        }
    }

    fun submitMessage(message: String, title: String, tags: List<String>) = viewModelScope.launch {
        writeNewMessageUseCase(state.notesCollection.collectionName, message, title, tags).onEach { resource ->
            parseMessagesResponse(resource)
        }.launchIn(viewModelScope)
    }

    fun syncNotes() = viewModelScope.launch {
        syncNotesUseCase(state.notesCollection.collectionName).onEach { resource ->
            parseMessagesResponse(resource)
        }.launchIn(viewModelScope)
    }

    fun removeMessage(message: Message) = viewModelScope.launch {
        deleteMessageUseCase(state.notesCollection.collectionName, message.id).onEach { resource ->
            parseMessagesResponse(resource)
        }.launchIn(viewModelScope)
    }

    fun editMessage(editedMessage: Message) = viewModelScope.launch {
        editMessageUseCase(state.notesCollection.collectionName, editedMessage).onEach { resource ->
            parseMessagesResponse(resource)
        }.launchIn(viewModelScope)
    }

    fun switchDisplayedCollection(notesCollection: NotesCollection) = viewModelScope.launch {
        settingsRepository.writeCurrentNotesCollectionId(notesCollection.serverAddress, notesCollection.collectionName)
    }

    private fun getMessages()  {
        getMessagesJob?.cancel()
        getMessagesJob = viewModelScope.launch {
            //delay(1000)
            println("MainViewModel getMessages aaaa")
            getMessagesUseCase(state.notesCollection.collectionName).onEach { resource ->
                parseMessagesResponse(resource)
            }.launchIn(viewModelScope)
    }}
}
