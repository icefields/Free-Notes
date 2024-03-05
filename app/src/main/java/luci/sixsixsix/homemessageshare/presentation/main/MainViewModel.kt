package luci.sixsixsix.homemessageshare.presentation.main

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import luci.sixsixsix.homemessageshare.common.Resource
import luci.sixsixsix.homemessageshare.di.WeakContext
import luci.sixsixsix.homemessageshare.domain.SettingsRepository
import luci.sixsixsix.homemessageshare.domain.models.Message
import luci.sixsixsix.homemessageshare.domain.use_case.DeleteMessageUseCase
import luci.sixsixsix.homemessageshare.domain.use_case.EditMessageUseCase
import luci.sixsixsix.homemessageshare.domain.use_case.GetMessagesUseCase
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
    private val settingsRepository: SettingsRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    var state by savedStateHandle.saveable { mutableStateOf(MessagesState()) }
        private set

    init {
        // replace with livedata or flow that listens to changes to settings
        getMessages()
        viewModelScope.launch {
            settingsRepository.settingsFlow.map { it.serverAddress }.collectLatest {
                getMessages()
            }
        }
        viewModelScope.launch {
            settingsRepository.settingsFlow.map { it.username }.collectLatest {
                getMessages()
            }
        }
    }

    private fun parseMessagesResponse(resource: Resource<List<Message>>) {
        when(resource) {
            is Resource.Success ->
                resource.data?.let { messages ->
                    state = state.copy(messages = messages.reversed())
                }
            is Resource.Error -> weakContext.get()?.let { context ->
                Toast.makeText(context, "Something went wrong,please try again", Toast.LENGTH_LONG).show()
            }
            is Resource.Loading ->
                state = state.copy(loading = resource.isLoading)
        }
    }

    fun submitMessage(message: String, title: String, tags: List<String>) = viewModelScope.launch {
        writeNewMessageUseCase(message, title, tags).onEach { resource ->
            parseMessagesResponse(resource)
        }.launchIn(viewModelScope)
    }

    fun removeMessage(message: Message) = viewModelScope.launch {
        deleteMessageUseCase(message.id).onEach { resource ->
            parseMessagesResponse(resource)
        }.launchIn(viewModelScope)
    }

    fun editMessage(editedMessage: Message) = viewModelScope.launch {
        editMessageUseCase(editedMessage).onEach { resource ->
            parseMessagesResponse(resource)
        }.launchIn(viewModelScope)
    }

    private fun getMessages() = viewModelScope.launch {
        getMessagesUseCase().onEach { resource ->
            parseMessagesResponse(resource)
        }.launchIn(viewModelScope)
    }
}
