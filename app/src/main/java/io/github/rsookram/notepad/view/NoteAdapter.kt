package io.github.rsookram.notepad.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.rsookram.notepad.R
import io.github.rsookram.notepad.data.Note

class NoteAdapter(
    private val onClick: (Note) -> Unit
) : ListAdapter<Note, NoteAdapter.Holder>(Diff()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
            .let { Holder(it as TextView) }
            .also { holder ->
                holder.itemView.setOnClickListener {
                    val position = holder.adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onClick(getItem(position))
                    }
                }
            }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.text = determineTitle(getItem(position).content)
    }

    override fun getItemId(position: Int): Long = getItem(position).id

    class Holder(val view: TextView) : RecyclerView.ViewHolder(view)
}

private class Diff : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
        oldItem.key == newItem.key

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
        determineTitle(oldItem.content) == determineTitle(newItem.content)
}

private fun determineTitle(content: String): String {
    var end = content.asSequence().indexOfFirst { it == '\n' }
    if (end < 1) {
        end = content.length
    }

    return content.substring(0, end)
}
