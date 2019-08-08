package io.github.rsookram.notepad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NoteViewModel {

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    init {
        _notes.value = listOf(
            Note("", "first", ""),
            Note("", "second", ""),
            Note("", "third", ""),
            Note("", "fourth", "")
        )
    }
}
