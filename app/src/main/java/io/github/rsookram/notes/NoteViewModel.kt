package io.github.rsookram.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.rsookram.notes.data.Note
import io.github.rsookram.notes.data.NoteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

class NoteViewModel(private val repository: NoteRepository) {

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    private val _openNote = Channel<Note>(Channel.CONFLATED)
    val openNote: ReceiveChannel<Note> = _openNote

    private val _deletedNote = Channel<Unit>(Channel.CONFLATED)
    val deletedNote: ReceiveChannel<Unit> = _deletedNote

    private var currentNote: Note? = null
    private var reversibleDelete: Note? = null

    init {
        repository.onUpdate = _notes::setValue
    }

    fun onNoteClicked(note: Note) {
        currentNote = note

        val n = repository.get(note.key) ?: return
        _openNote.offer(n)
    }

    fun onCreateNoteClicked() {
        val n = repository.next()
        currentNote = n
        _openNote.offer(n)

        // Clear and reversible deletions, since the newly created note may
        // have reused its key
        reversibleDelete = null
    }

    fun onSwipedAway(position: Int) {
        val note = _notes.value?.getOrNull(position) ?: return
        repository.delete(note)

        reversibleDelete = note
        _deletedNote.offer(Unit)
    }

    fun onUndoDeleteClicked() {
        val (key, content) = reversibleDelete ?: return
        repository.save(key, content)
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
        if (content.isNotBlank()) {
            repository.save(note.key, content)
        }
    }

    fun onCleared() {
        repository.onUpdate = null
    }
}
