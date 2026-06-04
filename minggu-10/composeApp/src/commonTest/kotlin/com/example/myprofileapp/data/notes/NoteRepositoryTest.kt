package com.example.myprofileapp.data.notes

import app.cash.turbine.test
import com.example.myprofileapp.testutil.FakeNoteRepository
import com.example.myprofileapp.testutil.testNote
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NoteRepositoryTest {
    @Test
    fun `insertNote adds a new note`() = runTest {
        val repository = FakeNoteRepository()

        repository.insertNote(title = "Anime Plan", content = "Watch weekly anime")

        assertEquals(1, repository.snapshot().size)
        assertEquals("Anime Plan", repository.snapshot().first().title)
    }

    @Test
    fun `updateNote changes title and content`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1)))

        repository.updateNote(id = 1, title = "Updated", content = "Updated content")

        val note = repository.getNoteById(1)
        assertEquals("Updated", note?.title)
        assertEquals("Updated content", note?.content)
    }

    @Test
    fun `deleteNote removes note by id`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1), testNote(id = 2)))

        repository.deleteNote(1)

        assertNull(repository.getNoteById(1))
        assertEquals(1, repository.snapshot().size)
    }

    @Test
    fun `toggleFavorite flips favorite state`() = runTest {
        val repository = FakeNoteRepository(listOf(testNote(id = 1, isFavorite = false)))

        repository.toggleFavorite(id = 1, isFavorite = false)

        assertTrue(repository.getNoteById(1)?.isFavorite == true)
    }

    @Test
    fun `searchNotes filters by title and content`() = runTest {
        val repository =
            FakeNoteRepository(
                listOf(
                    testNote(id = 1, title = "Kotlin", content = "KMP notes"),
                    testNote(id = 2, title = "Anime", content = "Watch list"),
                ),
            )

        repository.searchNotes("kmp").test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Kotlin", result.first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAllNotesByTitle returns ascending title order`() = runTest {
        val repository =
            FakeNoteRepository(
                listOf(
                    testNote(id = 1, title = "Zeta"),
                    testNote(id = 2, title = "Alpha"),
                ),
            )

        repository.getAllNotesByTitle().test {
            val result = awaitItem()
            assertEquals(listOf("Alpha", "Zeta"), result.map { it.title })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAllNotesByTitleDesc returns descending title order`() = runTest {
        val repository =
            FakeNoteRepository(
                listOf(
                    testNote(id = 1, title = "Alpha"),
                    testNote(id = 2, title = "Zeta"),
                ),
            )

        repository.getAllNotesByTitleDesc().test {
            val result = awaitItem()
            assertEquals(listOf("Zeta", "Alpha"), result.map { it.title })
            assertFalse(result.first().isFavorite)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
