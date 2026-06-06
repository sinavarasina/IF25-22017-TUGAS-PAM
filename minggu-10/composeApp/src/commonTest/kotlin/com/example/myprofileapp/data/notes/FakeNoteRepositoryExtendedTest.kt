package com.example.myprofileapp.data.notes

import app.cash.turbine.test
import com.example.myprofileapp.testutil.FakeNoteRepository
import com.example.myprofileapp.testutil.testNote
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FakeNoteRepositoryExtendedTest {
    @Test
    fun `getAllNotes emits initial notes`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1), testNote(id = 2)))

        repository.getAllNotes().test {
            assertEquals(2, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `insertNote increments id after max existing id`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 10)))

        repository.insertNote("Next", "Content")

        assertEquals(11, repository.snapshot().last().id)
    }

    @Test
    fun `searchNotes returns all notes for blank query`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1), testNote(id = 2)))

        repository.searchNotes("   ").test {
            assertEquals(2, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchNotes is case insensitive`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1, title = "KoTliN", content = "KMP")))

        repository.searchNotes("kotlin").test {
            assertEquals(1, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchNotes can match content`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1, title = "Title", content = "Hidden keyword")))

        repository.searchNotes("keyword").test {
            assertEquals("Title", awaitItem().first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateNote keeps other notes unchanged`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1, title = "One"), testNote(id = 2, title = "Two")))

        repository.updateNote(1, "Updated", "Updated content")

        assertEquals("Updated", repository.getNoteById(1)?.title)
        assertEquals("Two", repository.getNoteById(2)?.title)
    }

    @Test
    fun `updateNote with unknown id does nothing`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1, title = "One")))

        repository.updateNote(999, "Updated", "Updated content")

        assertEquals("One", repository.getNoteById(1)?.title)
        assertEquals(1, repository.snapshot().size)
    }

    @Test
    fun `toggleFavorite with unknown id does nothing`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1, isFavorite = false)))

        repository.toggleFavorite(999, isFavorite = false)

        assertTrue(repository.getNoteById(1)?.isFavorite == false)
    }

    @Test
    fun `deleteNote with unknown id does nothing`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1)))

        repository.deleteNote(999)

        assertEquals(1, repository.snapshot().size)
    }

    @Test
    fun `getNoteById returns null when missing`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1)))

        assertNull(repository.getNoteById(99))
    }
}
