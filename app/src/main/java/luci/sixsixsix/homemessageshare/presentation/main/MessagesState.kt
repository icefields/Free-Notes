package luci.sixsixsix.homemessageshare.presentation.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import luci.sixsixsix.homemessageshare.domain.models.Message

@Parcelize
data class MessagesState(
    val messages: List<Message> = listOf(),
    val loading: Boolean = false
): Parcelable
