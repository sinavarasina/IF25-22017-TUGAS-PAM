package com.example.myprofileapp.viewmodel.theme

import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeType
import kotlin.test.Test
import kotlin.test.assertEquals

class ThemeViewModelTest {
    @Test
    fun `default theme state is catppuccin dark`() {
        val viewModel = ThemeViewModel()

        assertEquals(ThemeType.CATPPUCCIN, viewModel.themeState.value.activeThemeType)
        assertEquals(ThemeMode.DARK, viewModel.themeState.value.themeMode)
    }

    @Test
    fun `setThemeType changes active theme`() {
        val viewModel = ThemeViewModel()

        viewModel.setThemeType(ThemeType.GRUVBOX)

        assertEquals(ThemeType.GRUVBOX, viewModel.themeState.value.activeThemeType)
    }

    @Test
    fun `setThemeType can switch back to catppuccin`() {
        val viewModel = ThemeViewModel()

        viewModel.setThemeType(ThemeType.GRUVBOX)
        viewModel.setThemeType(ThemeType.CATPPUCCIN)

        assertEquals(ThemeType.CATPPUCCIN, viewModel.themeState.value.activeThemeType)
    }

    @Test
    fun `setThemeMode changes mode`() {
        val viewModel = ThemeViewModel()

        viewModel.setThemeMode(ThemeMode.LIGHT)

        assertEquals(ThemeMode.LIGHT, viewModel.themeState.value.themeMode)
    }

    @Test
    fun `theme type and mode can be changed independently`() {
        val viewModel = ThemeViewModel()

        viewModel.setThemeType(ThemeType.GRUVBOX)
        viewModel.setThemeMode(ThemeMode.LIGHT)

        assertEquals(ThemeType.GRUVBOX, viewModel.themeState.value.activeThemeType)
        assertEquals(ThemeMode.LIGHT, viewModel.themeState.value.themeMode)
    }
}
