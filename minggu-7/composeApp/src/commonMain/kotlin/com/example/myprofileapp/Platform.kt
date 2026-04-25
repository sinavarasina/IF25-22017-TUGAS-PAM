package com.example.myprofileapp

import app.cash.sqldelight.db.SqlDriver

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun createDatabaseDriver(): SqlDriver
