package com.example.myprofileapp.di

import com.example.myprofileapp.createDatabaseDriver
import com.example.myprofileapp.data.ai.AiRepository
import com.example.myprofileapp.data.ai.GeminiService
import com.example.myprofileapp.data.news.HttpClientFactory
import com.example.myprofileapp.data.news.NewsApi
import com.example.myprofileapp.data.news.NewsRepository
import com.example.myprofileapp.data.notes.NoteRepository
import com.example.myprofileapp.data.notes.SqlDelightNoteRepository
import com.example.myprofileapp.data.settings.SettingsManager
import com.example.myprofileapp.db.NotesDatabase
import com.example.myprofileapp.platform.DeviceInfo
import com.example.myprofileapp.platform.NetworkMonitor
import com.example.myprofileapp.viewmodel.ai.AiViewModel
import com.example.myprofileapp.viewmodel.news.NewsViewModel
import com.example.myprofileapp.viewmodel.notes.NotesViewModel
import com.example.myprofileapp.viewmodel.profile.ProfileViewModel
import com.example.myprofileapp.viewmodel.theme.ThemeViewModel
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val settingsModule =
    module {
        single { Settings() }
        single { SettingsManager(settings = get()) }
    }

val databaseModule =
    module {
        single { NotesDatabase(createDatabaseDriver()) }
    }

val networkModule =
    module {
        single { HttpClientFactory.create() }
        single { NewsApi(client = get()) }
        single { GeminiService(client = get()) }
    }

val repositoryModule =
    module {
        single<NoteRepository> { SqlDelightNoteRepository(database = get()) }
        single { NewsRepository(api = get(), settings = get()) }
        single { AiRepository(geminiService = get()) }
    }

val platformModule =
    module {
        single { DeviceInfo() }
        single { NetworkMonitor() }
    }

val viewModelModule =
    module {
        single { ProfileViewModel() }
        single { ThemeViewModel() }
        single { NotesViewModel(repository = get(), settingsManager = get()) }
        single { NewsViewModel(repository = get()) }
        single { AiViewModel(repository = get()) }
    }

val appModules =
    listOf(
        settingsModule,
        databaseModule,
        networkModule,
        repositoryModule,
        platformModule,
        viewModelModule,
    )

val appModule =
    module {
        includes(
            settingsModule,
            databaseModule,
            networkModule,
            repositoryModule,
            platformModule,
            viewModelModule,
        )
    }
