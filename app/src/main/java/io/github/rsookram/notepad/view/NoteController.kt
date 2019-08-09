package io.github.rsookram.notepad.view

import com.airbnb.epoxy.EpoxyController
import io.github.rsookram.notepad.data.Note

class NoteController(private val onClick: (Note) -> Unit) : EpoxyController() {

    var notes: List<Note> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        notes.forEach { note ->
            noteItemView {
                id(note.key)
                title(determineTitle(note.content))
                onClick { onClick(note) }
            }
        }
    }

    private fun determineTitle(content: String): String {
        var end = content.asSequence().indexOfFirst { it == '\n' }
        if (end < 1) {
            end = content.length
        }

        return content.substring(0, end)
    }
}
