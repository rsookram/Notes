package io.github.rsookram.notepad.data

import android.content.SharedPreferences

class NoteRepository(private val prefs: SharedPreferences) {

    var onUpdate: ((List<Note>) -> Unit)? = null
        set(value) {
            value?.invoke(getNotes())
            field = value
        }

    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        onUpdate?.invoke(getNotes())
    }

    init {
        prefs.registerOnSharedPreferenceChangeListener(changeListener)
    }

    private fun getNotes(): List<Note> =
        prefs.all
            .mapNotNull { (key, content) ->
                if (content is String) {
                    Note(key, content)
                } else {
                    null
                }
            }
            .sortedBy { it.key.toInt() }

    fun next(): Note {
        val previousKey = prefs.all.keys.maxBy { it.toInt() }?.toInt() ?: 0
        val key = (previousKey + 1).toString()

        return Note(key, "")
    }

    fun get(key: String): Note? {
        val content = prefs.getString(key, null) ?: return null
        return Note(key, content)
    }

    fun save(key: String, content: String) {
        prefs.edit()
            .putString(key, content)
            .apply()
    }

    fun delete(note: Note) {
        prefs.edit().remove(note.key).apply()
    }
}
