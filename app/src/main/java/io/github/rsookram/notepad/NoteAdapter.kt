package io.github.rsookram.notepad

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter : ListAdapter<Note, NoteAdapter.Holder>(Diff()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
            .let { Holder(it as TextView) }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.text = getItem(position).title
    }

    override fun getItemId(position: Int): Long =
        getItem(position).key.hashCode().toLong()

    class Holder(val view: TextView) : RecyclerView.ViewHolder(view)
}

private class Diff : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
        oldItem.key == newItem.key

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
        oldItem.title == newItem.title
}
