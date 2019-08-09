package io.github.rsookram.notepad

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import io.github.rsookram.notepad.view.CollapseInterceptor
import io.github.rsookram.notepad.view.NoteController
import io.github.rsookram.notepad.view.SwipeDismissCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_note.*
import me.saket.inboxrecyclerview.page.SimplePageStateChangeCallbacks

class MainActivity : AppCompatActivity() {

    private lateinit var vm: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vm = lastNonConfigurationInstance as? NoteViewModel ?: NoteViewModel(app.repository)

        vm.openNote.observe(this, Observer { event ->
            val note = event?.getContentIfNotHandled()
            if (note != null) {
                note_content.bind(note)
                note_content.scrollTo(0, 0)
                note_list.expandItem(note.id)
            }
        })

        vm.deletedNote.observe(this, Observer { event ->
            if (event.getContentIfNotHandled() != null) {
                Snackbar.make(note_list, R.string.deleted_note, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo_deletion) { vm.onUndoDeleteClicked() }
                    .show()
            }
        })

        note_list.setExpandablePage(expandable_page)

        val controller = NoteController(vm::onNoteClicked, vm::onCreateNoteClicked)

        vm.notes.observe(this, Observer { notes ->
            if (notes != null) {
                controller.notes = notes
            }
        })

        note_list.adapter = controller.adapter

        ItemTouchHelper(SwipeDismissCallback { holder ->
            // Subtract one to account for the header
            val position = note_list.getChildAdapterPosition(holder.itemView) - 1
            vm.onSwipedAway(position)
        }).attachToRecyclerView(note_list)

        expandable_page.pullToCollapseInterceptor = CollapseInterceptor(note_content)

        expandable_page.addStateChangeCallbacks(object : SimplePageStateChangeCallbacks() {
            override fun onPageAboutToCollapse(collapseAnimDuration: Long) {
                vm.onNoteClosed(note_edit_text.text.toString())

                val imm = getSystemService<InputMethodManager>()!!
                imm.hideSoftInputFromWindow(note_edit_text.windowToken, 0)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        vm.onStop(note_edit_text.text.toString())
    }

    override fun onDestroy() {
        super.onDestroy()

        if (!isChangingConfigurations) {
            vm.onCleared()
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any = vm

    override fun onBackPressed() {
        if (expandable_page.isExpandedOrExpanding) {
            note_list.collapse()
        } else {
            super.onBackPressed()
        }
    }
}
