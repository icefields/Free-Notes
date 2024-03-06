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
}