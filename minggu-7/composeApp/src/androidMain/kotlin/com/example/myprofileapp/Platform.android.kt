package com.example.myprofileapp

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

lateinit var appContext: Context

actual fun createDatabaseDriver(): SqlDriver = AndroidSqliteDriver(NotesDatabase.Schema, appContext, "notes.db")
