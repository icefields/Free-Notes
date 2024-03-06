package luci.sixsixsix.homemessageshare.presentation

import android.app.Application
import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.parcelize.Parcelize
import luci.sixsixsix.homemessageshare.di.WeakContext
import luci.sixsixsix.homemessageshare.domain.SettingsRepository
import luci.sixsixsix.homemessageshare.domain.models.Message
import luci.sixsixsix.homemessageshare.domain.models.Settings
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val weakContext: WeakContext,
    private val settingsRepository: SettingsRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    var state by mutableStateOf(Settings())

    init {
        runBlocking {
            state = state.copy(isMaterialYouEnabled = settingsRepository.isMaterialYouEnabled())
        }

        viewModelScope.launch {
            settingsRepository.settingsFlow.collectLatest {
                state = it
            }
        }
    }

    fun toggleMaterialYou(enable: Boolean) = viewModelScope.launch {
        settingsRepository.toggleMaterialYou(enable)
    }

    fun setServer(server: String) = viewModelScope.launch {
        settingsRepository.writeServerAddress(server)
//        if (server != state.serverAddress) {
//            //getMessages()
//            state = state.copy(serverAddress = server)
//        }
    }

    fun setUsername(username: String) = viewModelScope.launch {
        settingsRepository.writeUsername(username)
//        if (username != state.serverAddress) {
//            // getMessages()
//            // TODO only after getting messages, if everything is ok update the username
//            //  otherwise app will always crash on invalid username (ie username==log)
//            state = state.copy(username = username)
//        }
    }
}

@Parcelize
data class SettingsState(
    val settings: Settings = Settings()
//    val isMaterialYouEnabled: Boolean = false,
//    val serverAddress: String = "",
//    val username: String = ""
): Parcelable
