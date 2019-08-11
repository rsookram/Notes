package io.github.rsookram.notes.data

data class Note(val key: String, val content: String) {

    val id = key.hashCode().toLong()
}
