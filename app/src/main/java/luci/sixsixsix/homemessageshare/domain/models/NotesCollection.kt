package luci.sixsixsix.homemessageshare.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotesCollection(
    val collectionName: String,
    val dateCreated: String,
    val dateModified: String,
    val colour: String,
    val tags: List<String> = listOf(),
    val serverAddress: String,
    val appTheme: String
): Parcelable
