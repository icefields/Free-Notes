package luci.sixsixsix.homemessageshare.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import luci.sixsixsix.homemessageshare.domain.models.Message

@Entity
data class NoteEntity(
    @PrimaryKey val id: String,
    val dateCreated: String,
    val dateModified: String,
    val note: String,
    val title: String,
    val username: String,
)

fun NoteEntity.toNote() = Message(
    id = id,
    dateCreated = dateCreated,
    dateModified = dateModified,
    message = note,
    title = title,
    tags = listOf()
)

fun Message.toNoteEntity(username: String) = NoteEntity(
    id = id,
    dateCreated = dateCreated,
    dateModified = dateModified,
    note = message,
    title = title,
    username = username
)
