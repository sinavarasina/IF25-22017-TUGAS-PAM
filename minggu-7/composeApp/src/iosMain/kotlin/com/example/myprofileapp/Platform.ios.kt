package com.example.myprofileapp

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.myprofileapp.db.NotesDatabase
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun createDatabaseDriver(): SqlDriver = NativeSqliteDriver(NotesDatabase.Schema, "notes.db")
