package io.github.rsookram.notes.view

import com.airbnb.epoxy.EpoxyController
import io.github.rsookram.notes.data.Note

private const val ID_HEADER = 0L
private const val ID_CREATE_NOTE = Long.MAX_VALUE

class NoteController(
    private val onClick: (Note) -> Unit,
    private val onCreateClick: () -> Unit
) : EpoxyController() {

    var notes: List<Note> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    fun isFixed(itemId: Long): Boolean =
        itemId == ID_HEADER || itemId == ID_CREATE_NOTE

    override fun buildModels() {
        headerView { id(ID_HEADER) }

        notes.forEach { note ->
            noteItemView {
                id(note.id)
                title(determineTitle(note.content))
                onClick { onClick(note) }
            }
        }

        createNoteView {
            id(ID_CREATE_NOTE)
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
