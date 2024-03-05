package luci.sixsixsix.homemessageshare.domain

import kotlinx.coroutines.flow.Flow
import luci.sixsixsix.homemessageshare.domain.models.Message

interface MessagesRepository {
    suspend fun getMessages(): List<Message>
    suspend fun submitMessage(title: String, message: String): List<Message>
    suspend fun deleteMessage(id: String): List<Message>
}
