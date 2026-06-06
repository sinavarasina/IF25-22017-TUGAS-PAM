package com.example.myprofileapp.data.notes

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class NoteModelTest {
    @Test
    fun `note stores all constructor values`() {
        val note = Note(id = 7, title = "Title", content = "Content", isFavorite = true)

        assertEquals(7, note.id)
        assertEquals("Title", note.title)
        assertEquals("Content", note.content)
        assertTrue(note.isFavorite)
    }

    @Test
    fun `note copy can change favorite`() {
        val note = Note(id = 1, title = "A", content = "B", isFavorite = false)

        val updated = note.copy(isFavorite = true)

        assertFalse(note.isFavorite)
        assertTrue(updated.isFavorite)
    }

    @Test
    fun `note equality uses all fields`() {
        val first = Note(id = 1, title = "A", content = "B", isFavorite = false)
        val second = Note(id = 1, title = "A", content = "B", isFavorite = false)
        val third = Note(id = 1, title = "A", content = "B", isFavorite = true)

        assertEquals(first, second)
        assertNotEquals(first, third)
    }

    @Test
    fun `dummy notes contains three notes`() {
        assertEquals(3, dummyNotes.size)
    }

    @Test
    fun `dummy notes has stable ids`() {
        assertEquals(listOf(1, 2, 3), dummyNotes.map { it.id })
    }

    @Test
    fun `dummy notes contains favorite and non favorite notes`() {
        assertTrue(dummyNotes.any { it.isFavorite })
        assertTrue(dummyNotes.any { !it.isFavorite })
    }

    @Test
    fun `dummy notes contain expected titles`() {
        val titles = dummyNotes.map { it.title }

        assertTrue("Watchlist (Weekly)" in titles)
        assertTrue("Menunggu Batch" in titles)
        assertTrue("Avoid List" in titles)
    }
}
