package com.example.myprofileapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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

@Composable
actual fun SystemAppearance(
    isDark: Boolean,
    statusBarColor: Color,
) {
// actually unnecessary but i just let the compiler happy lmao
}
