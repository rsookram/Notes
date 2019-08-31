package io.github.rsookram.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.rsookram.notes.data.Note
import io.github.rsookram.notes.data.NoteDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class NoteViewModel(private val dao: NoteDao) : ViewModel() {

    val notes = dao.notes()

    private val _openNote = Channel<Note>(Channel.CONFLATED)
    val openNote: ReceiveChannel<Note> = _openNote

    private val _deletedNote = Channel<Unit>(Channel.CONFLATED)
    val deletedNote: ReceiveChannel<Unit> = _deletedNote

    private var currentNote: Note? = null
    private var reversibleDelete: Note? = null

    fun onNoteClicked(note: Note) {
        viewModelScope.launch {
            currentNote = note

            val n = dao.get(note.id) ?: return@launch
            _openNote.offer(n)
        }
    }

    fun onCreateNoteClicked() {
        viewModelScope.launch {
            // Clear reversible deletions, since the newly created note may
            // have reused its key
            reversibleDelete = null

            val n = dao.next()
            currentNote = n
            _openNote.offer(n)
        }
    }

    fun onSwipedAway(position: Int) {
        viewModelScope.launch {
            val note = notes.value?.getOrNull(position) ?: return@launch
            reversibleDelete = note

            dao.delete(note)

            _deletedNote.offer(Unit)
        }
    }

    fun onUndoDeleteClicked() {
        viewModelScope.launch {
            val (id, content) = reversibleDelete ?: return@launch
            dao.save(id, content)
        }
    }

    fun onStop(content: String) {
        val current = currentNote ?: return
        saveIfNotEmpty(current, content)
    }

    fun onNoteClosed(content: String) {
        val current = currentNote ?: return

        saveIfNotEmpty(current, content)
        currentNote = null
    }

    private fun saveIfNotEmpty(note: Note, content: String) {
        viewModelScope.launch {
            if (content.isNotBlank()) {
                dao.save(note.id, content)
            }
        }
    }
}
