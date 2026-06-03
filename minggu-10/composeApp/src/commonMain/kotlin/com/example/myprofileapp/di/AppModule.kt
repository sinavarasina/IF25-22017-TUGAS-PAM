package com.example.myprofileapp.di

import com.example.myprofileapp.createDatabaseDriver
import com.example.myprofileapp.data.news.HttpClientFactory
import com.example.myprofileapp.data.news.NewsApi
import com.example.myprofileapp.data.news.NewsRepository
import com.example.myprofileapp.data.notes.NoteRepository
import com.example.myprofileapp.data.settings.SettingsManager
import com.example.myprofileapp.db.NotesDatabase
import com.example.myprofileapp.platform.DeviceInfo
import com.example.myprofileapp.platform.NetworkMonitor
import com.example.myprofileapp.viewmodel.news.NewsViewModel
import com.example.myprofileapp.viewmodel.notes.NotesViewModel
import com.example.myprofileapp.viewmodel.profile.ProfileViewModel
import com.example.myprofileapp.viewmodel.theme.ThemeViewModel
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val appModule =
    module {
        single { Settings() }
        single { SettingsManager(settings = get()) }

        single { NotesDatabase(createDatabaseDriver()) }
        single { NoteRepository(database = get()) }

        single { HttpClientFactory.create() }
        single { NewsApi(client = get()) }
        single { NewsRepository(api = get(), settings = get()) }

        single { DeviceInfo() }
        single { NetworkMonitor() }

        single { ProfileViewModel() }
        single { ThemeViewModel() }
        single { NotesViewModel(repository = get(), settingsManager = get()) }
        single { NewsViewModel(repository = get()) }
    }
