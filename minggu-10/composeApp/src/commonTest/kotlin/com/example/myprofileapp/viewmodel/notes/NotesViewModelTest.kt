package com.example.myprofileapp.viewmodel.notes

import app.cash.turbine.test
import com.example.myprofileapp.data.settings.SettingsManager
import com.example.myprofileapp.data.settings.SortOrder
import com.example.myprofileapp.testutil.FakeNoteRepository
import com.example.myprofileapp.testutil.testNote
import com.russhwolf.settings.Settings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {
    @Test
    fun `setSearchQuery updates search query state`() = runTest {
        val viewModel = createViewModel(repository = FakeNoteRepository())

        viewModel.setSearchQuery("kotlin")

        assertEquals("kotlin", viewModel.searchQuery.value)
    }

    @Test
    fun `setSortOrder updates and persists sort order`() = runTest {
        val settingsManager = SettingsManager(Settings())
        val viewModel = createViewModel(repository = FakeNoteRepository(), settingsManager = settingsManager)

        viewModel.setSortOrder(SortOrder.TITLE_ASC)

        assertEquals(SortOrder.TITLE_ASC, viewModel.sortOrder.value)
        assertEquals(SortOrder.TITLE_ASC, settingsManager.sortOrder)
    }

    @Test
    fun `notes emits repository notes`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1, title = "First")))
        val viewModel = createViewModel(repository = repository)

        viewModel.notes.test {
            awaitItem()
            val notes = awaitItem()
            assertEquals(1, notes.size)
            assertEquals("First", notes.first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `notes flow reacts to search query`() = runTest {
        val repository =
            FakeNoteRepository(
                listOf(
                    testNote(id = 1, title = "Kotlin", content = "KMP"),
                    testNote(id = 2, title = "Anime", content = "Watch list"),
                ),
            )
        val viewModel = createViewModel(repository = repository)

        viewModel.notes.test {
            awaitItem()
            awaitItem()

            viewModel.setSearchQuery("anime")
            advanceUntilIdle()

            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Anime", result.first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addNote inserts note through repository`() = runTest {
        val repository = FakeNoteRepository()
        val viewModel = createViewModel(repository = repository)

        viewModel.notes.test {
            awaitItem()
            viewModel.addNote("New", "New content")
            advanceUntilIdle()

            val notes = awaitItem()
            assertEquals(1, notes.size)
            assertEquals("New", notes.first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite updates note favorite state`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1, isFavorite = false)))
        val viewModel = createViewModel(repository = repository)

        viewModel.notes.test {
            awaitItem()
            awaitItem()
            viewModel.toggleFavorite(1)
            advanceUntilIdle()

            val notes = awaitItem()
            assertTrue(notes.first().isFavorite)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteNote removes note`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1)))
        val viewModel = createViewModel(repository = repository)

        viewModel.notes.test {
            awaitItem()
            awaitItem()
            viewModel.deleteNote(1)
            advanceUntilIdle()

            val notes = awaitItem()
            assertTrue(notes.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun TestScope.createViewModel(
        repository: FakeNoteRepository,
        settingsManager: SettingsManager = SettingsManager(Settings()),
    ): NotesViewModel =
        NotesViewModel(
            repository = repository,
            settingsManager = settingsManager,
            coroutineScope = this,
        )
}
