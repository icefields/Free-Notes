package luci.sixsixsix.homemessageshare.presentation.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import luci.sixsixsix.homemessageshare.common.Constants
import luci.sixsixsix.homemessageshare.domain.models.Message

@Parcelize
data class MessagesState(
    val username: String = Constants.OFFLINE_USERNAME,
    val messages: List<Message> = listOf(),
    val loading: Boolean = false
): Parcelable
