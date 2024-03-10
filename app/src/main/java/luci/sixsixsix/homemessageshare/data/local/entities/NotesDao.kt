package luci.sixsixsix.homemessageshare.data.local.entities

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<NoteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("""DELETE FROM noteentity WHERE id == :noteId""")
    suspend fun deleteNote(noteId: String)

    @Query("DELETE FROM noteentity")
    suspend fun clearNotes()

    @Query("""SELECT * FROM noteentity WHERE username == :username""")
    suspend fun getNotes(username: String): List<NoteEntity>

    @Query("""SELECT * FROM noteentity WHERE username == :username""")
    fun getNotesLiveData(username: String): LiveData<List<NoteEntity>>

    @Query("""SELECT * FROM collectionentity""")
    suspend fun getCollections(): List<CollectionEntity>

    @Query("""SELECT * FROM collectionentity WHERE id == :collectionId""")
    suspend fun getCollection(collectionId: String): CollectionEntity

    @Query("""SELECT * FROM collectionentity WHERE id == :collectionId""")
    fun getCollectionsLiveData(collectionId: String): LiveData<List<CollectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity)

    @Query("""DELETE FROM collectionentity WHERE id == :collectionId""")
    suspend fun deleteCollection(collectionId: String)

    @Query("DELETE FROM collectionentity")
    suspend fun clearCollections()

    @Query("""SELECT * FROM collectionentity""")
    fun getCollectionsLiveData(): LiveData<List<CollectionEntity>>
}
