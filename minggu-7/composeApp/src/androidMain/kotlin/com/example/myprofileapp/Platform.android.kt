package com.example.myprofileapp

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.myprofileapp.db.NotesDatabase

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

lateinit var appContext: Context

actual fun createDatabaseDriver(): SqlDriver = AndroidSqliteDriver(NotesDatabase.Schema, appContext, "notes.db")
