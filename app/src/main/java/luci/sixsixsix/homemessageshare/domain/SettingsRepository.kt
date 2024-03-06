package luci.sixsixsix.homemessageshare.domain

import androidx.lifecycle.asFlow
import kotlinx.coroutines.flow.Flow
import luci.sixsixsix.homemessageshare.domain.models.Settings

typealias Success = Boolean

interface SettingsRepository {
    val settingsFlow: Flow<Settings>
    suspend fun getServerAddress(returnDefaultIfNull: Boolean = true): String
    suspend fun writeServerAddress(serverAddress: String): Success
    suspend fun getUsername(): String?
    suspend fun writeUsername(username: String): Success
    suspend fun isMaterialYouEnabled(): Boolean
    suspend fun toggleMaterialYou(enable: Boolean): Boolean
}

