package luci.sixsixsix.homemessageshare.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection

@Entity
data class CollectionEntity(
    @PrimaryKey val id: String,
    val dateCreated: String,
    val dateModified: String,
    val colour: String,
    //val tags: List<String>,
    val serverAddress: String,
    val appTheme: String
)

fun CollectionEntity.toCollection() = NotesCollection(
    id = id,
    dateCreated = dateCreated,
    dateModified = dateModified,
    colour = colour,
    serverAddress = serverAddress,
    appTheme = appTheme
)

fun NotesCollection.toCollectionEntity(username: String) = CollectionEntity(
    id = id,
    dateCreated = dateCreated,
    dateModified = dateModified,
    colour = colour,
    serverAddress = serverAddress,
    appTheme = appTheme
)
