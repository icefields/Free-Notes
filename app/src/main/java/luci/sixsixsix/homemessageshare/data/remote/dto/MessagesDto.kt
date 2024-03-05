package luci.sixsixsix.homemessageshare.data.remote.dto

import com.google.gson.annotations.SerializedName
import luci.sixsixsix.homemessageshare.domain.models.Message

data class MessageDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("date")
    val date: String,
    @SerializedName("message")
    val message: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("tag")
    val tags: List<String>?
)

data class MessagesDto(
    @SerializedName("data")
    val `data`: List<MessageDto>
)

fun MessageDto.toMessage() = Message(
    id = id ?: "",
    title = title ?: "",
    message = message ?: "",
    tags = tags ?: listOf(),
    date = date
)
