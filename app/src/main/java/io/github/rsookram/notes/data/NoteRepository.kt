package io.github.rsookram.notes.data

class NoteRepository(private val dao: NoteDao) {

    val notes = dao.notes()

    suspend fun next(): Note = dao.next()

    suspend fun get(id: Long): Note? = dao.get(id)

    suspend fun save(id: Long, content: String) {
        dao.insert(Note(id, content))
    }

    suspend fun delete(note: Note) {
        dao.delete(note)
    }
}
