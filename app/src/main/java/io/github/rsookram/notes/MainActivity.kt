package io.github.rsookram.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import io.github.rsookram.notes.view.CollapseInterceptor
import io.github.rsookram.notes.view.NoteController
import io.github.rsookram.notes.view.SwipeDismissCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import me.saket.inboxrecyclerview.page.SimplePageStateChangeCallbacks

class MainActivity : AppCompatActivity() {

    private lateinit var vm: NoteViewModel

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vm = lastNonConfigurationInstance as? NoteViewModel ?: NoteViewModel(app.repository)

        scope.launch {
            vm.openNote.consumeAsFlow().collect { note ->
                note_content.bind(note)
                note_content.scrollTo(0, 0)
                note_list.expandItem(note.id)
            }
        }

        scope.launch {
            vm.deletedNote.consumeAsFlow().collect {
                Snackbar.make(note_list, R.string.deleted_note, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo_deletion) { vm.onUndoDeleteClicked() }
                    .show()
            }
        }

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
            override fun onPageExpanded() {
                // If there's nothing to view, open the keyboard to start
                // writing
                if (note_content.content.isEmpty()) {
                    note_content.showKeyboard()
                }
            }

            override fun onPageAboutToCollapse(collapseAnimDuration: Long) {
                vm.onNoteClosed(note_content.content)
                note_content.hideKeyboard()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        vm.onStop(note_content.content)
    }

    override fun onDestroy() {
        super.onDestroy()

        scope.cancel()

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
