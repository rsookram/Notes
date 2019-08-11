package io.github.rsookram.notes

import io.github.rsookram.notes.data.Note
import io.github.rsookram.notes.data.NoteRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) {

    val notes = repository.notes

    private val _openNote = Channel<Note>(Channel.CONFLATED)
    val openNote: ReceiveChannel<Note> = _openNote

    private val _deletedNote = Channel<Unit>(Channel.CONFLATED)
    val deletedNote: ReceiveChannel<Unit> = _deletedNote

    private var currentNote: Note? = null
    private var reversibleDelete: Note? = null

    private val scope = MainScope()

    fun onNoteClicked(note: Note) {
        currentNote = note

        scope.launch {
            val n = repository.get(note.id) ?: return@launch
            _openNote.offer(n)
        }
    }

    fun onCreateNoteClicked() {
        scope.launch {
            val n = repository.next()
            currentNote = n
            _openNote.offer(n)

            // Clear and reversible deletions, since the newly created note may
            // have reused its key
            reversibleDelete = null
        }
    }

    fun onSwipedAway(position: Int) {
        scope.launch {
            val note = notes.value?.getOrNull(position) ?: return@launch
            repository.delete(note)

            reversibleDelete = note
            _deletedNote.offer(Unit)
        }
    }

    fun onUndoDeleteClicked() {
        scope.launch {
            val (id, content) = reversibleDelete ?: return@launch
            repository.save(id, content)
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
        scope.launch {
            if (content.isNotBlank()) {
                repository.save(note.id, content)
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }
}
