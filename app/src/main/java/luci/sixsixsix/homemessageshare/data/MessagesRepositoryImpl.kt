package luci.sixsixsix.homemessageshare.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import luci.sixsixsix.homemessageshare.common.Resource
import luci.sixsixsix.homemessageshare.data.local.NotesDatabase
import luci.sixsixsix.homemessageshare.data.local.entities.NoteEntity
import luci.sixsixsix.homemessageshare.data.local.entities.toNote
import luci.sixsixsix.homemessageshare.data.local.entities.toNoteEntity
import luci.sixsixsix.homemessageshare.data.remote.MainNetwork
import luci.sixsixsix.homemessageshare.data.remote.dto.toMessage
import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import luci.sixsixsix.homemessageshare.domain.models.Message
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.Throws

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class MessagesRepositoryImpl @Inject constructor(
    private val api: MainNetwork,
    db: NotesDatabase
): MessagesRepository {
    private val dao = db.dao

    override suspend fun getMessages(username: String) =
        flow {
            println("MessagesRepository getMessages aaa ${username}")
            emit(Resource.Loading(true))
            val notesDb = dao.getNotes(username).map { it.toNote() }
            emit(Resource.Success(notesDb))
            if (username.isNotBlank()) {
                val notesNetwork = api.getMessages(username).data.map { it.toMessage() }
                dao.insertNotes(notesNetwork.map { it.toNoteEntity(username) })
                emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
            }
            emit(Resource.Loading(false))
        }.catch { e -> emit(Resource.Error(exception = e)) }

    override suspend fun submitMessage(
        username: String,
        note: String,
        title: String,
        tags: List<String>
    )  = flow {
        emit(Resource.Loading(true))
        val tempId = UUID.randomUUID().toString()
        dao.insertNote(NoteEntity(
                id = tempId,
                note = note,
                title = title,
                username = username,
                dateModified = LocalDateTime.now().toString(),
                dateCreated = LocalDateTime.now().toString()
        ))
        emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))

        if (username.isNotBlank()) {
            val notesNetwork = api.writeMessage(
                id = tempId,
                username = username,
                title = title,
                message = note,
                tags = tags
            ).data.map { it.toMessage() }
            dao.insertNotes(notesNetwork.map { it.toNoteEntity(username) })
            emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
        }

        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) }

    override suspend fun editMessage(username: String, note: Message)  =
        flow {
            emit(Resource.Loading(true))
            dao.insertNote(note.toNoteEntity(username))
            emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
            if (username.isNotBlank()) {
                val notesNetwork = api.editMessage(
                    username = username,
                    id = note.id,
                    title = note.title,
                    message = note.message
                ).data.map { it.toMessage() }
                dao.insertNotes(notesNetwork.map { it.toNoteEntity(username) })
                emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
            }
            emit(Resource.Loading(false))
        }.catch { e -> emit(Resource.Error(exception = e)) }

    override suspend fun deleteMessage(username: String, id: String) = flow {
        emit(Resource.Loading(true))
        dao.deleteNote(id)
        emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
        if (username.isNotBlank()) {
            val notesNetwork = api.deleteMessage(
                username = username,
                id = id
            ).data.map { it.toMessage() }
            dao.insertNotes(notesNetwork.map { it.toNoteEntity(username) })
            emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
        }
        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) }

    /**
     * syncs all offline messages to server, using the current username if any is set
     */
    @Throws(Exception::class)
    override suspend fun syncNotes(username: String) = flow {
        // TODO create exception
        if (username.isNullOrBlank()) throw Exception("username not set exception")

        // get messages from database
        emit(Resource.Loading(true))
        val notesDb = dao.getNotes(username).map { it.toNote() }
        // get messages from server
        var notesDtoNetwork = api.getMessages(username)//.data.map { it.toMessage() }
        notesDb.forEach { dbNote ->
            // if db note is not present on server, send it
            if ( !(notesDtoNetwork.data.map { it.id }.contains(dbNote.id)) ) {
                notesDtoNetwork = api.writeMessage(
                    id = dbNote.id,
                    username = username,
                    title = dbNote.title,
                    message = dbNote.message,
                    tags = dbNote.tags
                )
            }
        }
        // save the new network notes
        dao.insertNotes(notesDtoNetwork.data.map { it.toMessage() }.map { it.toNoteEntity(username) })
        emit(Resource.Success(dao.getNotes(username).map { it.toNote() }))
        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) }
}
