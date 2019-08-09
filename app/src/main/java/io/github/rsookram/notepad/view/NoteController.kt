package io.github.rsookram.notepad.view

import com.airbnb.epoxy.EpoxyController
import io.github.rsookram.notepad.data.Note

class NoteController(
    private val onClick: (Note) -> Unit,
    private val onCreateClick: () -> Unit
) : EpoxyController() {

    var notes: List<Note> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        headerView { id(0) }

        notes.forEach { note ->
            noteItemView {
                id(note.id)
                title(determineTitle(note.content))
                onClick { onClick(note) }
            }
        }

        createNoteView {
            id(Long.MAX_VALUE)
            onClick { onCreateClick() }
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
