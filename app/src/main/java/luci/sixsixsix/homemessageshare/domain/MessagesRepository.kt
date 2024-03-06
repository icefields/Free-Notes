package luci.sixsixsix.homemessageshare.domain

import kotlinx.coroutines.flow.Flow
import luci.sixsixsix.homemessageshare.common.Resource
import luci.sixsixsix.homemessageshare.domain.models.Message

interface MessagesRepository {
    suspend fun getMessages(username: String): Flow<Resource<List<Message>>>
    suspend fun submitMessage(username: String, noteStr: String, title: String, tags: List<String>): Flow<Resource<List<Message>>>
    suspend fun editMessage(username: String, noteStr: Message): Flow<Resource<List<Message>>>
    suspend fun deleteMessage(username: String, id: String): Flow<Resource<List<Message>>>
    suspend fun syncNotes(username: String): Flow<Resource<List<Message>>>

}
