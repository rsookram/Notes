package io.github.rsookram.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.rsookram.notes.data.Note
import io.github.rsookram.notes.data.NoteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class NoteViewModel(private val dao: NoteDao) : ViewModel() {

    val notes = dao.notes()

    @ExperimentalCoroutinesApi
    private val _openNote = BroadcastChannel<Note>(1)
    @ExperimentalCoroutinesApi
    val openNote: ReceiveChannel<Note> get() = _openNote.openSubscription()

    @ExperimentalCoroutinesApi
    private val _deletedNote = BroadcastChannel<Unit>(1)
    @ExperimentalCoroutinesApi
    val deletedNote: ReceiveChannel<Unit> get() = _deletedNote.openSubscription()

    private var currentNote: Note? = null
    private var reversibleDelete: Note? = null

    @ExperimentalCoroutinesApi
    fun onNoteClicked(note: Note) {
        viewModelScope.launch {
            currentNote = note

            val n = dao.get(note.id) ?: return@launch
            _openNote.offer(n)
        }
    }

    @ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
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
