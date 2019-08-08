package io.github.rsookram.notepad.data

data class Note(val key: String, val content: String) {

    val id = key.hashCode().toLong()
}
