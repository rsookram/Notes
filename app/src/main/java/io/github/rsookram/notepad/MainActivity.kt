package io.github.rsookram.notepad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        note_list.setExpandablePage(expandable_page)

        val adapter = NoteAdapter()
        adapter.submitList(listOf(
            Note("", "first", ""),
            Note("", "second", ""),
            Note("", "third", ""),
            Note("", "fourth", "")
        ))
        note_list.adapter = adapter
    }
}
