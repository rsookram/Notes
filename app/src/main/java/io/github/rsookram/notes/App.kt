package io.github.rsookram.notes

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import io.github.rsookram.notes.data.NoteRepository

class App : Application() {

    val repository by lazy {
        NoteRepository(PreferenceManager.getDefaultSharedPreferences(this))
    }
}

val Context.app: App
    get() = applicationContext as App
