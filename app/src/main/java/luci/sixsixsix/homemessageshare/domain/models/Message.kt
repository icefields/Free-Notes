package luci.sixsixsix.homemessageshare.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val id: String,
    val dateCreated: String,
    val dateModified: String,
    val message: String,
    val title: String,
    val tags: List<String>,
    val custom: Custom? = null
): Parcelable

@Parcelize
class Custom: Parcelable