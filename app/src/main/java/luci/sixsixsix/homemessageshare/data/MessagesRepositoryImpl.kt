package luci.sixsixsix.homemessageshare.data

import luci.sixsixsix.homemessageshare.data.remote.MainNetwork
import luci.sixsixsix.homemessageshare.data.remote.dto.toMessage
import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagesRepositoryImpl @Inject constructor(
    private val api: MainNetwork
): MessagesRepository {
    override suspend fun getMessages() =
        api.getMessages().data.map { it.toMessage() }


    override suspend fun submitMessage(title: String, message: String) =
        api.writeMessage(title = title, message = message).data.map { it.toMessage() }


    override suspend fun deleteMessage(id: String) =
        api.deleteMessage(id = id).data.map { it.toMessage() }
}
