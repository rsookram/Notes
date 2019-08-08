package io.github.rsookram.notepad

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.updatePadding
import androidx.core.widget.NestedScrollView
import kotlinx.android.synthetic.main.view_note.view.*

class NoteView(context: Context, attrs: AttributeSet) : NestedScrollView(context, attrs) {

    init {
        isFillViewport = true

        val verticalPadding = resources.getDimensionPixelSize(R.dimen.note_vertical_padding)
        updatePadding(top = verticalPadding, bottom = verticalPadding)
        clipToPadding = false

        View.inflate(context, R.layout.view_note, this)

        note_edit_text.addTextChangedListener(ApplyTitleSpanTextWatcher())
    }

    fun bind(note: Note) {
        note_edit_text.setText(note.content)
    }
}
