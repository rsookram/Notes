package io.github.rsookram.notepad

data class Note(val key: String, val title: String, val content: String) {

    val id = key.hashCode().toLong()
}
