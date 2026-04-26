package com.example.myprofileapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import app.cash.sqldelight.db.SqlDriver

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun createDatabaseDriver(): SqlDriver

@Composable
expect fun SystemAppearance(
    isDark: Boolean,
    statusBarColor: Color,
)
