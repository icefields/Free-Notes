package luci.sixsixsix.homemessageshare.presentation.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import luci.sixsixsix.homemessageshare.di.WeakContext
import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import luci.sixsixsix.homemessageshare.domain.models.Message
import luci.sixsixsix.homemessageshare.domain.use_case.DeleteMessageUseCase
import luci.sixsixsix.homemessageshare.domain.use_case.GetMessagesUseCase
import luci.sixsixsix.homemessageshare.domain.use_case.WriteNewMessageUseCase
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val weakContext: WeakContext,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val writeNewMessageUseCase: WriteNewMessageUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    var state by savedStateHandle.saveable { mutableStateOf(MessagesState()) }
        private set

    init {
        getServer()
        getMessages()
    }

    fun submitMessages(title: String, message: String) = viewModelScope.launch {
        writeNewMessageUseCase(title, message).onEach { messages ->
            state = state.copy(messages = messages.reversed())
        }.launchIn(viewModelScope)
    }

    fun removeMessages(message: Message) = viewModelScope.launch {
        deleteMessageUseCase(message.id).onEach { messages ->
            state = state.copy(messages = messages.reversed())
        }.launchIn(viewModelScope)
    }

    private fun getMessages() {
        getMessagesUseCase().onEach { messages ->
            state = state.copy(messages = messages.reversed())
        }.launchIn(viewModelScope)
    }

    fun setServer(server: String) = viewModelScope.launch {
       weakContext.get()?.let {
           StorageManager.writeServerAddress(it, server)
       }
    }

    private fun getServer() = viewModelScope.launch {
        weakContext.get()?.let { context ->
            StorageManager.getServerAddress(context)?.let { serverAddress ->
                state = state.copy(serverAddress = serverAddress)
            }
        }
    }
}

object StorageManager {
    private const val KEY_WORKER_PREFERENCE = "{prefix}KEY_WORKER_PREFERENCE"
    private const val KEY_WORKER_PREFERENCE_ID = "{prefix}downloadWorkerId"

    suspend fun getServerAddress(context: Context):String? = context
        .getSharedPreferences(KEY_WORKER_PREFERENCE, Context.MODE_PRIVATE)
        .getString(KEY_WORKER_PREFERENCE_ID, null)


    @SuppressLint("ApplySharedPref")
    suspend fun writeServerAddress(
        context: Context,
        newWorkerId: String
    ): String = withContext(Dispatchers.IO) {
        val sharedPreferences = context.getSharedPreferences(KEY_WORKER_PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString(KEY_WORKER_PREFERENCE_ID, newWorkerId)
            commit()
        }
        return@withContext newWorkerId
    }
}