package luci.sixsixsix.homemessageshare.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val id: String,
    val date: String,
    val message: String,
    val title: String,
    val tags: List<String>,
    val custom: Custom? = null
): Parcelable

@Parcelize
class Custom: Parcelable