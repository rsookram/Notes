package io.github.rsookram.notes.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class NoteDao {

    @Query("SELECT id, content FROM note ORDER BY id")
    abstract fun notes(): LiveData<List<Note>>

    suspend fun next() = Note(nextId() ?: 1, "")

    @Query("SELECT MAX(id) + 1 FROM note")
    protected abstract suspend fun nextId(): Long?

    @Query("SELECT id, content FROM note WHERE id = :id")
    abstract suspend fun get(id: Long): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(note: Note)

    @Delete
    abstract suspend fun delete(note: Note)
}
