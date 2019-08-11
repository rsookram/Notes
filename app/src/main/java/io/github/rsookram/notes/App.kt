package io.github.rsookram.notes

import android.app.Application
import android.content.Context
import androidx.room.Room
import io.github.rsookram.notes.data.AppDatabase

class App : Application() {

    private val database by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "note.db")
            .build()
    }

    val dao by lazy { database.noteDao() }
}

val Context.app: App
    get() = applicationContext as App
