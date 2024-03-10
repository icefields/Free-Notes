package luci.sixsixsix.homemessageshare.presentation

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import luci.sixsixsix.homemessageshare.common.Resource
import luci.sixsixsix.homemessageshare.di.WeakContext
import luci.sixsixsix.homemessageshare.domain.CollectionsRepository
import luci.sixsixsix.homemessageshare.domain.SettingsRepository
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection
import luci.sixsixsix.homemessageshare.domain.models.Settings
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val weakContext: WeakContext,
    private val settingsRepository: SettingsRepository,
    private val collectionsRepository: CollectionsRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    var state by mutableStateOf(Settings())

    init {
        runBlocking {
            state = state.copy(isMaterialYouEnabled = settingsRepository.isMaterialYouEnabled())
        }

        viewModelScope.launch {
            settingsRepository.settingsFlow.collectLatest {
                println("SettingsViewModel aaa new settings received ${it.username}")
                state = it
            }
        }
    }

    fun toggleMaterialYou(enable: Boolean) = viewModelScope.launch {
        settingsRepository.toggleMaterialYou(enable)
    }

    fun setServer(server: String) = viewModelScope.launch {
        settingsRepository.writeServerAddress(server)
    }

    fun setUsername(username: String) = viewModelScope.launch {
        println("setUsername $username aaaa")
        settingsRepository.writeUsername(username)
        collectionsRepository.writeCollection(NotesCollection(
            collectionName = username,
            serverAddress = state.serverAddress,
            dateCreated = "",
            dateModified = "",
            tags = listOf(),
            appTheme = if (state.isMaterialYouEnabled) "MaterialYouDark" else "Dark",
            colour = "ffff00"
        )).collect { resource ->
            when(resource) {
                is Resource.Success ->
                    resource.data?.let { collections ->
                        //state = state.copy(messages = messages.reversed())
                    }
                is Resource.Error -> weakContext.get()?.let { context -> {
                    Toast.makeText(context, "Something went wrong,please try again ${resource.message}", Toast.LENGTH_LONG).show()
                    println("MainViewModel setUsername parseMessagesResponse aaaa ${resource.message}")
                }
                }
                is Resource.Loading -> { }
            }
        }
    }
}

//@Parcelize
//data class SettingsState(
//    val settings: Settings = Settings()
////    val isMaterialYouEnabled: Boolean = false,
////    val serverAddress: String = "",
////    val username: String = ""
//): Parcelable
