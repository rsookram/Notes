package io.github.rsookram.notepad.data

import android.content.SharedPreferences

class NoteRepository(private val prefs: SharedPreferences) {

    var onUpdate: ((List<Note>) -> Unit)? = null
        set(value) {
            value?.invoke(getNotes())
            field = value
        }

    init {
        prefs.registerOnSharedPreferenceChangeListener { _, _ ->
            onUpdate?.invoke(getNotes())
        }
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

    fun create(content: String): Note {
        val previousKey = prefs.all.keys.maxBy { it.toInt() }?.toInt() ?: 0
        val key = (previousKey + 1).toString()

        val note = Note(key, content)
        save(key, content)

        return note
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
