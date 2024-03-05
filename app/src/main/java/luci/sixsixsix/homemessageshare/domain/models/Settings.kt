package luci.sixsixsix.homemessageshare.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Settings(
    val isMaterialYouEnabled: Boolean = false,
    val serverAddress: String = "",
    val username: String = ""
): Parcelable
