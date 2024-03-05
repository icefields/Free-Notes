package luci.sixsixsix.homemessageshare.data

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import luci.sixsixsix.homemessageshare.common.Resource
import luci.sixsixsix.homemessageshare.data.remote.MainNetwork
import luci.sixsixsix.homemessageshare.data.remote.dto.toMessage
import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import luci.sixsixsix.homemessageshare.domain.SettingsRepository
import luci.sixsixsix.homemessageshare.domain.models.Message
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagesRepositoryImpl @Inject constructor(
    private val api: MainNetwork,
    private val settingsRepository: SettingsRepository
): MessagesRepository {
    override suspend fun getMessages() = flow {
        emit(Resource.Loading(true))
        emit(Resource.Success(api.getMessages(getUsername()).data.map { it.toMessage() }))
        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) }

    override suspend fun submitMessage(
        message: String,
        title: String,
        tags: List<String>
    )  = flow {
        emit(Resource.Loading(true))
        emit(Resource.Success(api.writeMessage(
            username = getUsername(),
            title = title,
            message = message,
            tags = tags
        ).data.map { it.toMessage() }))
        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) }

    override suspend fun editMessage(message: Message)  = flow {
        emit(Resource.Loading(true))
        emit(Resource.Success(api.editMessage(
            username = getUsername(),
            id = message.id,
            title = message.title,
            message = message.message
        ).data.map { it.toMessage() }))
        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) }

    override suspend fun deleteMessage(id: String) = flow {
        emit(Resource.Loading(true))
        emit(Resource.Success(
            api.deleteMessage(
                username = getUsername(),
                id = id
            ).data.map { it.toMessage() }
        ))
        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) }

    private suspend fun getUsername() =
        URLEncoder.encode(settingsRepository.getUsername(), "UTF-8")
}
