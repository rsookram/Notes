package io.github.rsookram.notepad

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager

class App : Application() {

    val repository by lazy {
        NoteRepository(PreferenceManager.getDefaultSharedPreferences(this))
    }
}

val Context.app: App
    get() = applicationContext as App
