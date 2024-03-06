package luci.sixsixsix.homemessageshare.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.distinctUntilChanged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import luci.sixsixsix.homemessageshare.common.Constants.DEBUG_SERVER
import luci.sixsixsix.homemessageshare.di.WeakContext
import luci.sixsixsix.homemessageshare.domain.SettingsRepository
import luci.sixsixsix.homemessageshare.domain.Success
import luci.sixsixsix.homemessageshare.domain.models.Settings
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY_STORAGE = "{prefix}KEY_WORKER_PREFERENCE"
private const val KEY_WORKER_PREFERENCE_ID = "{prefix}downloadWorkerId"
private const val KEY_USERNAME_PREFERENCE_ID = "{prefix}username.id.storage"
private const val KEY_MATERIAL_YOU_PREFERENCE_ID = "{prefix}materiayouonoff.id.storage"

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val weakContext: WeakContext
): SettingsRepository {
    private val _settingsLiveData = MutableLiveData(Settings())
    val settingsLiveData: LiveData<Settings> = _settingsLiveData
    override val settingsFlow = settingsLiveData.distinctUntilChanged().asFlow()

    private fun getSharedPreferences() =
        weakContext.get()?.getSharedPreferences(KEY_STORAGE, Context.MODE_PRIVATE)

    @SuppressLint("ApplySharedPref")
    private suspend fun writeString(
        key: String,
        serverAddress: String
    ): Success =
        getSharedPreferences()?.let { sharedPreferences ->
            withContext(Dispatchers.IO) {
                val editor = sharedPreferences.edit()
                editor.apply {
                    putString(key, serverAddress)
                    commit()
                }
                return@withContext true
            }
        } ?: false

    override suspend fun getServerAddress(returnDefaultIfNull: Boolean): String =
        getSharedPreferences()?.getString(KEY_WORKER_PREFERENCE_ID, null) ?: run {
            if (returnDefaultIfNull) {
                DEBUG_SERVER
            } else ""
        }

    @SuppressLint("ApplySharedPref")
    override suspend fun writeServerAddress(
        serverAddress: String
    ) = writeString(KEY_WORKER_PREFERENCE_ID, serverAddress).also {
        _settingsLiveData.value = settingsLiveData.value?.copy(serverAddress = serverAddress)
    }

    override suspend fun getUsername() =
        getSharedPreferences()?.getString(KEY_USERNAME_PREFERENCE_ID, null)

    override suspend fun writeUsername(username: String): Success =
        writeString(KEY_USERNAME_PREFERENCE_ID, username).also {
            _settingsLiveData.value = settingsLiveData.value?.copy(username = username)
        }

    override suspend fun isMaterialYouEnabled() =
        getSharedPreferences()?.getString(KEY_MATERIAL_YOU_PREFERENCE_ID, "true") == "true"

    override suspend fun toggleMaterialYou(enable: Boolean) = enable.also {
        writeString(KEY_MATERIAL_YOU_PREFERENCE_ID, it.toString())
        _settingsLiveData.value = settingsLiveData.value?.copy(isMaterialYouEnabled = enable)
    }
}
