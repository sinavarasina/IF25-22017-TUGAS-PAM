package com.example.myprofileapp.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class RoutesTest {
    @Test
    fun `note detail stores note id`() {
        val route = Screen.NoteDetail(noteId = 42)

        assertEquals(42, route.noteId)
    }

    @Test
    fun `edit note stores note id`() {
        val route = Screen.EditNote(noteId = 12)

        assertEquals(12, route.noteId)
    }

    @Test
    fun `news detail stores article id`() {
        val route = Screen.NewsDetail(articleId = 9)

        assertEquals(9, route.articleId)
    }

    @Test
    fun `note detail equality uses id`() {
        assertEquals(Screen.NoteDetail(1), Screen.NoteDetail(1))
        assertNotEquals(Screen.NoteDetail(1), Screen.NoteDetail(2))
    }

    @Test
    fun `main screen objects are stable singletons`() {
        assertEquals(Screen.Notes, Screen.Notes)
        assertEquals(Screen.Favorites, Screen.Favorites)
        assertEquals(Screen.Profile, Screen.Profile)
        assertEquals(Screen.Settings, Screen.Settings)
        assertEquals(Screen.AIAssistant, Screen.AIAssistant)
        assertEquals(Screen.NewsList, Screen.NewsList)
    }
}
