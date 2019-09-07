package io.github.rsookram.notes

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import io.github.rsookram.notes.view.CollapseInterceptor
import io.github.rsookram.notes.view.NoteController
import io.github.rsookram.notes.view.SwipeDismissCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.saket.inboxrecyclerview.page.SimplePageStateChangeCallbacks

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Suppress("UNCHECKED_CAST")
    private val vm: NoteViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                app.createNoteViewModel() as T
        }
    }

    private val scope = MainScope()

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.openNote.consumeAsFlow()
            .onEach { note ->
                note_content.bind(note)
                note_content.scrollTo(0, 0)
                note_list.expandItem(note.id)
            }
            .launchIn(scope)

        vm.deletedNote.consumeAsFlow()
            .onEach {
                Snackbar.make(note_list, R.string.deleted_note, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo_deletion) { vm.onUndoDeleteClicked() }
                    .show()
            }
            .launchIn(scope)

        note_list.setExpandablePage(expandable_page)

        val controller = NoteController(vm::onNoteClicked, vm::onCreateNoteClicked)

        vm.notes.observe(this, Observer { notes ->
            if (notes != null) {
                controller.notes = notes
            }
        })

        note_list.adapter = controller.adapter

        ItemTouchHelper(SwipeDismissCallback(canSwipe = { !controller.isFixed(it) }) { holder ->
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

        onBackPressedDispatcher.addCallback {
            if (expandable_page.isExpandedOrExpanding) {
                note_list.collapse()
            } else {
                finish()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        vm.onStop(note_content.content)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
