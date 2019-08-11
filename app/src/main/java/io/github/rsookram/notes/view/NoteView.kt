package io.github.rsookram.notes.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.view.updatePadding
import androidx.core.widget.NestedScrollView
import io.github.rsookram.notes.R
import io.github.rsookram.notes.data.Note
import kotlinx.android.synthetic.main.view_note.view.*

class NoteView(context: Context, attrs: AttributeSet) : NestedScrollView(context, attrs) {

    val content: String
        get() = note_edit_text.text.toString()

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

    fun showKeyboard() {
        note_edit_text.requestFocus()

        val imm = context.getSystemService<InputMethodManager>()!!
        imm.showSoftInput(note_edit_text, 0)
    }

    fun hideKeyboard() {
        val imm = context.getSystemService<InputMethodManager>()!!
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
