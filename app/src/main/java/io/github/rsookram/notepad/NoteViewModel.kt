package io.github.rsookram.notepad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.rsookram.notepad.data.Note
import io.github.rsookram.notepad.data.NoteRepository

class NoteViewModel(private val repository: NoteRepository) {

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    private val _openNote = MutableLiveData<Event<Note>>()
    val openNote: LiveData<Event<Note>> = _openNote

    private val _deletedNote = MutableLiveData<Event<Unit>>()
    val deletedNote: LiveData<Event<Unit>> = _deletedNote

    private var currentNote: Note? = null
    private var reversibleDelete: Note? = null

    init {
        repository.onUpdate = _notes::setValue
    }

    fun onNoteClicked(note: Note) {
        currentNote = note

        val n = repository.get(note.key)
        if (n != null) {
            _openNote.value = Event(n)
        }
    }

    fun onCreateNoteClicked() {
        val n = repository.next()
        currentNote = n
        _openNote.value = Event(n)

        // Clear and reversible deletions, since the newly created note may
        // have reused its key
        reversibleDelete = null
    }

    fun onSwipedAway(position: Int) {
        val note = _notes.value?.getOrNull(position) ?: return
        repository.delete(note)

        reversibleDelete = note
        _deletedNote.value = Event(Unit)
    }

    fun onUndoDeleteClicked() {
        val toUndo = reversibleDelete ?: return
        repository.save(toUndo.key, toUndo.content)
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
