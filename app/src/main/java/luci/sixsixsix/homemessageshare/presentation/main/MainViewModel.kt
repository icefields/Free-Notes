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
import luci.sixsixsix.homemessageshare.common.getDateString
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
    var state by savedStateHandle.saveable { mutableStateOf(MessagesState()) }
        private set

    var collectionsState by savedStateHandle.saveable { mutableStateOf(CollectionsState()) }
        private set


    private var getMessagesJob: Job? = null

    init {
        // replace with livedata or flow that listens to changes to settings
        viewModelScope.launch {
            settingsRepository.settingsFlow.collectLatest {
                println("MainViewModel - new settings aaaa ${it.username}")
                state = state.copy(username = it.username)
                getMessages()
            }
        }

        viewModelScope.launch {
            collectionsRepository.collectionsLiveData.asFlow().collectLatest {
                println("MainViewModel - new collections aaaa ${it.size}")
                if (it.isNotEmpty()) {
                    collectionsState = collectionsState.copy(collections = it)
                } else if (state.username.isNotBlank() && state.username != Constants.OFFLINE_USERNAME) {
                    collectionsRepository.writeCollection(
                        NotesCollection(
                            collectionName = state.username,
                            dateCreated = getDateString(),
                            dateModified = getDateString(),
                            colour = "ffff00",
                            tags = listOf(),
                            serverAddress = settingsRepository.getServerAddress(true),
                            appTheme = "dark"
                        )
                    ).collect { resource ->
                        when(resource) {
                            is Resource.Success -> { }
                            is Resource.Error -> { }
                            is Resource.Loading -> { }
                        }
                    }
                }
            }
        }
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
        writeNewMessageUseCase(state.username, message, title, tags).onEach { resource ->
            parseMessagesResponse(resource)
        }.launchIn(viewModelScope)
    }

    fun syncNotes() = viewModelScope.launch {
        syncNotesUseCase(state.username).onEach { resource ->
            parseMessagesResponse(resource)
        }.launchIn(viewModelScope)
    }

    fun removeMessage(message: Message) = viewModelScope.launch {
        deleteMessageUseCase(state.username, message.id).onEach { resource ->
            parseMessagesResponse(resource)
        }.launchIn(viewModelScope)
    }

    fun editMessage(editedMessage: Message) = viewModelScope.launch {
        editMessageUseCase(state.username, editedMessage).onEach { resource ->
            parseMessagesResponse(resource)
        }.launchIn(viewModelScope)
    }

    fun changeCollection(notesCollection: NotesCollection) = viewModelScope.launch {
        settingsRepository.writeServerAddress(notesCollection.serverAddress)
        settingsRepository.writeUsername(notesCollection.collectionName)
    }

    private fun getMessages()  {
        getMessagesJob?.cancel()
        getMessagesJob = viewModelScope.launch {
            //delay(1000)
            println("MainViewModel getMessages aaaa")
            getMessagesUseCase(state.username).onEach { resource ->
                parseMessagesResponse(resource)
            }.launchIn(viewModelScope)
    }}
}
