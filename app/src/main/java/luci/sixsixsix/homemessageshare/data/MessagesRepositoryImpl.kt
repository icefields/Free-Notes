package luci.sixsixsix.homemessageshare.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import luci.sixsixsix.homemessageshare.common.Constants.OFFLINE_USERNAME
import luci.sixsixsix.homemessageshare.common.Resource
import luci.sixsixsix.homemessageshare.data.local.NotesDatabase
import luci.sixsixsix.homemessageshare.data.local.entities.NoteEntity
import luci.sixsixsix.homemessageshare.data.local.entities.toNote
import luci.sixsixsix.homemessageshare.data.local.entities.toNoteEntity
import luci.sixsixsix.homemessageshare.data.remote.MainNetwork
import luci.sixsixsix.homemessageshare.data.remote.dto.toMessage
import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import luci.sixsixsix.homemessageshare.domain.SettingsRepository
import luci.sixsixsix.homemessageshare.domain.models.Message
import java.net.URLEncoder
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class MessagesRepositoryImpl @Inject constructor(
    private val api: MainNetwork,
    db: NotesDatabase,
    settingsRepository: SettingsRepository
): MessagesRepository {
    private val dao = db.dao
    private val usernameFlow = settingsRepository.settingsFlow.map { it.username }

    override suspend fun getMessages() = usernameFlow.flatMapLatest { user ->
        val username = if(user.isNullOrBlank()) OFFLINE_USERNAME else user
        flow {
            emit(Resource.Loading(true))
            val notesDb = dao.getNotes(username).map { it.toNote() }
            emit(Resource.Success(notesDb))
            val notesNetwork = api.getMessages(username).data.map { it.toMessage() }
            dao.insertNotes(notesNetwork.map { it.toNoteEntity(username) })
            emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
            emit(Resource.Loading(false))
        }.catch { e -> emit(Resource.Error(exception = e)) }
    }

    override suspend fun submitMessage(
        message: String,
        title: String,
        tags: List<String>
    )  = usernameFlow.flatMapLatest { user ->
        val username = if(user.isNullOrBlank()) OFFLINE_USERNAME else user
    flow {
        emit(Resource.Loading(true))
        val tempId = UUID.randomUUID().toString()
        dao.insertNote(NoteEntity(
            id = tempId,
            note = message,
            title = title,
            username = username,
            dateModified = LocalDateTime.now().toString(),
            dateCreated = LocalDateTime.now().toString()
        ))
        emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))

        val notesNetwork = api.writeMessage(
            username = username,
            title = title,
            message = message,
            tags = tags
        ).data.map { it.toMessage() }

        dao.insertNotes(notesNetwork.map { it.toNoteEntity(username) })
        dao.deleteNote(tempId)
        emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) } }

    override suspend fun editMessage(message: Message)  = usernameFlow.flatMapLatest { user ->
        val username = if(user.isNullOrBlank()) OFFLINE_USERNAME else user
        flow {
            emit(Resource.Loading(true))
            dao.insertNote(message.toNoteEntity(username))
            emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
            val notesNetwork = api.editMessage(
                username = username,
                id = message.id,
                title = message.title,
                message = message.message
            ).data.map { it.toMessage() }
            dao.insertNotes(notesNetwork.map { it.toNoteEntity(username) })
            emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
            emit(Resource.Loading(false))
        }.catch { e -> emit(Resource.Error(exception = e)) }
    }

    override suspend fun deleteMessage(id: String) = usernameFlow.flatMapLatest { user ->
        val username = if(user.isNullOrBlank()) OFFLINE_USERNAME else user
        flow {
            emit(Resource.Loading(true))
            dao.deleteNote(id)
            emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
            val notesNetwork = api.deleteMessage(
                username = username,
                id = id
            ).data.map { it.toMessage() }
            dao.insertNotes(notesNetwork.map { it.toNoteEntity(username) })
            emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
            emit(Resource.Loading(false))
        }.catch { e -> emit(Resource.Error(exception = e)) }
    }
}
