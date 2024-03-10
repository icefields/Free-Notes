package luci.sixsixsix.homemessageshare.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection

@Entity
data class CollectionEntity(
    @PrimaryKey val id: String, // = "$serverAddress$collectionName"
    val dateCreated: String,
    val dateModified: String,
    val colour: String,
    //val tags: List<String>,
    val collectionName: String,
    val serverAddress: String,
    val appTheme: String
)

fun collectionEntityId(collectionName: String, serverAddress: String) =
    "$serverAddress$collectionName"


fun CollectionEntity.toNotesCollection() = NotesCollection(
    collectionName = collectionName,
    dateCreated = dateCreated,
    dateModified = dateModified,
    colour = colour,
    serverAddress = serverAddress,
    appTheme = appTheme
)

fun NotesCollection.toCollectionEntity() = CollectionEntity(
    collectionName = collectionName,
    id = collectionEntityId(serverAddress, collectionName),
    dateCreated = dateCreated,
    dateModified = dateModified,
    colour = colour,
    serverAddress = serverAddress,
    appTheme = appTheme
)
