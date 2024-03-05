package luci.sixsixsix.homemessageshare.domain

import kotlinx.coroutines.flow.Flow
import luci.sixsixsix.homemessageshare.common.Resource
import luci.sixsixsix.homemessageshare.domain.models.Message

interface MessagesRepository {
    suspend fun getMessages(): Flow<Resource<List<Message>>>
    suspend fun submitMessage(message: String, title: String, tags: List<String>): Flow<Resource<List<Message>>>
    suspend fun editMessage(message: Message): Flow<Resource<List<Message>>>
    suspend fun deleteMessage(id: String): Flow<Resource<List<Message>>>
}
