package com.example.myprofileapp.viewmodel.notes

import com.example.myprofileapp.data.notes.NoteRepository
import com.example.myprofileapp.data.settings.SettingsManager
import com.example.myprofileapp.data.settings.SortOrder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NotesViewModelMockKTest {
    private lateinit var repository: NoteRepository
    private lateinit var settingsManager: SettingsManager

    @BeforeTest
    fun setup() {
        repository = mockk(relaxed = true)
        settingsManager = mockk(relaxed = true)

        every { settingsManager.sortOrder } returns SortOrder.DATE_DESC
        every { settingsManager.sortOrder = any() } just Runs
        every { repository.getAllNotes() } returns flowOf(emptyList())
        every { repository.getAllNotesOldest() } returns flowOf(emptyList())
        every { repository.getAllNotesByTitle() } returns flowOf(emptyList())
        every { repository.getAllNotesByTitleDesc() } returns flowOf(emptyList())
        every { repository.searchNotes(any()) } returns flowOf(emptyList())
    }

    @Test
    fun `addNote calls repository insertNote`() = runTest {
        coEvery { repository.insertNote(any(), any()) } just Runs
        val viewModel = createViewModel()

        viewModel.addNote("Title", "Content")
        testScheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.insertNote("Title", "Content") }
    }

    @Test
    fun `updateNote calls repository updateNote`() = runTest {
        coEvery { repository.updateNote(any(), any(), any()) } just Runs
        val viewModel = createViewModel()

        viewModel.updateNote(7, "Updated", "Updated content")
        testScheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.updateNote(7, "Updated", "Updated content") }
    }

    @Test
    fun `deleteNote calls repository deleteNote`() = runTest {
        coEvery { repository.deleteNote(any()) } just Runs
        val viewModel = createViewModel()

        viewModel.deleteNote(3)
        testScheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.deleteNote(3) }
    }

    @Test
    fun `setSortOrder updates state and persists setting`() = runTest {
        val viewModel = createViewModel()

        viewModel.setSortOrder(SortOrder.TITLE_DESC)

        assertEquals(SortOrder.TITLE_DESC, viewModel.sortOrder.value)
        io.mockk.verify(exactly = 1) { settingsManager.sortOrder = SortOrder.TITLE_DESC }
    }

    private fun kotlinx.coroutines.test.TestScope.createViewModel(): NotesViewModel =
        NotesViewModel(
            repository = repository,
            settingsManager = settingsManager,
            coroutineScope = this,
        )
}
