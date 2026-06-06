package com.example.myprofileapp.viewmodel.notes

import app.cash.turbine.test
import com.example.myprofileapp.data.settings.SettingsManager
import com.example.myprofileapp.data.settings.SortOrder
import com.example.myprofileapp.testutil.FakeNoteRepository
import com.example.myprofileapp.testutil.testNote
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelExtendedTest {
    @Test
    fun `initial sort order is read from settings`() = runTest {
        val settingsManager = SettingsManager(MapSettings())
        settingsManager.sortOrder = SortOrder.TITLE_DESC

        val viewModel = createViewModel(FakeNoteRepository(), settingsManager)

        assertEquals(SortOrder.TITLE_DESC, viewModel.sortOrder.value)
    }

    @Test
    fun `title ascending sort emits sorted notes`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1, title = "Zeta"), testNote(id = 2, title = "Alpha")))
        val viewModel = createViewModel(repository)

        viewModel.notes.test {
            awaitItem()
            awaitItem()
            viewModel.setSortOrder(SortOrder.TITLE_ASC)
            advanceUntilIdle()

            assertEquals(listOf("Alpha", "Zeta"), awaitItem().map { it.title })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `title descending sort emits sorted notes`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1, title = "Alpha"), testNote(id = 2, title = "Zeta")))
        val viewModel = createViewModel(repository)

        viewModel.notes.test {
            awaitItem()
            awaitItem()
            viewModel.setSortOrder(SortOrder.TITLE_DESC)
            advanceUntilIdle()

            assertEquals(listOf("Zeta", "Alpha"), awaitItem().map { it.title })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `blank search query keeps all notes`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1), testNote(id = 2)))
        val viewModel = createViewModel(repository)

        viewModel.notes.test {
            awaitItem()
            assertEquals(2, awaitItem().size)

            viewModel.setSearchQuery("missing")
            advanceUntilIdle()
            assertEquals(0, awaitItem().size)

            viewModel.setSearchQuery("   ")
            advanceUntilIdle()
            assertEquals(2, awaitItem().size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search query can match content`() = runTest {
        val repository =
            FakeNoteRepository(
                listOf(
                    testNote(id = 1, title = "One", content = "No match"),
                    testNote(id = 2, title = "Two", content = "Contains keyword"),
                ),
            )
        val viewModel = createViewModel(repository)

        viewModel.notes.test {
            awaitItem()
            awaitItem()
            viewModel.setSearchQuery("keyword")
            advanceUntilIdle()

            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Two", result.first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateNote changes note in emitted state`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1, title = "Old", content = "Old content")))
        val viewModel = createViewModel(repository)

        viewModel.notes.test {
            awaitItem()
            awaitItem()
            viewModel.updateNote(1, "New", "New content").join()
            advanceUntilIdle()

            val result = awaitItem()
            assertEquals("New", result.first().title)
            assertEquals("New content", result.first().content)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getNoteById returns note after notes are collected`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 7, title = "Found")))
        val viewModel = createViewModel(repository)

        viewModel.notes.test {
            awaitItem()
            awaitItem()
            assertEquals("Found", viewModel.getNoteById(7)?.title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getNoteById returns null for missing note`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1)))
        val viewModel = createViewModel(repository)

        viewModel.notes.test {
            awaitItem()
            awaitItem()
            assertNull(viewModel.getNoteById(404))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite unknown id keeps notes unchanged`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1, isFavorite = false)))
        val viewModel = createViewModel(repository)

        viewModel.notes.test {
            awaitItem()
            awaitItem()
            viewModel.toggleFavorite(404).join()
            advanceUntilIdle()

            assertTrue(repository.snapshot().first().isFavorite == false)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun TestScope.createViewModel(
        repository: FakeNoteRepository,
        settingsManager: SettingsManager = SettingsManager(MapSettings()),
    ): NotesViewModel =
        NotesViewModel(
            repository = repository,
            settingsManager = settingsManager,
            coroutineScope = backgroundScope,
        )
}
