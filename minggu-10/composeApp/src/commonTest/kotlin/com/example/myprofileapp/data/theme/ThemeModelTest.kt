package com.example.myprofileapp.data.theme

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ThemeModelTest {
    @Test
    fun `theme state defaults are stable`() {
        val state = ThemeState()

        assertEquals(ThemeType.CATPPUCCIN, state.activeThemeType)
        assertEquals(ThemeMode.DARK, state.themeMode)
    }

    @Test
    fun `theme state copy changes type only`() {
        val state = ThemeState().copy(activeThemeType = ThemeType.GRUVBOX)

        assertEquals(ThemeType.GRUVBOX, state.activeThemeType)
        assertEquals(ThemeMode.DARK, state.themeMode)
    }

    @Test
    fun `theme state copy changes mode only`() {
        val state = ThemeState().copy(themeMode = ThemeMode.LIGHT)

        assertEquals(ThemeType.CATPPUCCIN, state.activeThemeType)
        assertEquals(ThemeMode.LIGHT, state.themeMode)
    }

    @Test
    fun `theme states compare by values`() {
        assertEquals(ThemeState(ThemeType.CATPPUCCIN, ThemeMode.DARK), ThemeState())
        assertNotEquals(ThemeState(ThemeType.GRUVBOX, ThemeMode.DARK), ThemeState())
    }
}
