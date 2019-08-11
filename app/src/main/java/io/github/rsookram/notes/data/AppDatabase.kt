package io.github.rsookram.notes.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [Note::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}
