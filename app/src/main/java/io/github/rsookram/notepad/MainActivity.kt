package io.github.rsookram.notepad

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_note.*
import me.saket.inboxrecyclerview.page.InterceptResult
import me.saket.inboxrecyclerview.page.SimplePageStateChangeCallbacks

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        note_list.setExpandablePage(expandable_page)

        val adapter = NoteAdapter { note ->
            note_content.bind(note)
            note_list.expandItem(note.id)
        }
        adapter.submitList(listOf(
            Note("", "first", ""),
            Note("", "second", ""),
            Note("", "third", ""),
            Note("", "fourth", "")
        ))
        note_list.adapter = adapter

        expandable_page.pullToCollapseInterceptor = { _, _, upwardPull ->
            val directionInt = if (upwardPull) +1 else -1
            val canScrollFurther = note_content.canScrollVertically(directionInt)
            if (canScrollFurther) InterceptResult.INTERCEPTED else InterceptResult.IGNORED
        }

        expandable_page.addStateChangeCallbacks(object : SimplePageStateChangeCallbacks() {
            override fun onPageAboutToCollapse(collapseAnimDuration: Long) {
                val imm = getSystemService<InputMethodManager>()!!
                imm.hideSoftInputFromWindow(note_edit_text.windowToken, 0)
            }
        })
    }

    override fun onBackPressed() {
        if (expandable_page.isExpandedOrExpanding) {
            note_list.collapse()
        } else {
            super.onBackPressed()
        }
    }
}
