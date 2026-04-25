package com.example.myprofileapp

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.myprofileapp.db.NotesDatabase

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

lateinit var appContext: Context

actual fun createDatabaseDriver(): SqlDriver = AndroidSqliteDriver(NotesDatabase.Schema, appContext, "notes.db")

@Composable
actual fun SystemAppearance(
    isDark: Boolean,
    statusBarColor: Color,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        LaunchedEffect(isDark) {
            var context = view.context
            while (context is ContextWrapper) {
                if (context is Activity) break
                context = context.baseContext
            }
            val window = (context as? Activity)?.window ?: return@LaunchedEffect

            window.statusBarColor = android.graphics.Color.TRANSPARENT

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
        }
    }
}
