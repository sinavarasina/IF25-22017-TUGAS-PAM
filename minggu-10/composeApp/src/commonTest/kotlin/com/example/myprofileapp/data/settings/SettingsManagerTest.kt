package com.example.myprofileapp.data.settings

import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeType
import com.russhwolf.settings.MapSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsManagerTest {
    @Test
    fun `default settings are catppuccin dark and newest sort`() {
        val manager = SettingsManager(MapSettings())

        assertEquals(ThemeType.CATPPUCCIN, manager.themeType)
        assertEquals(ThemeMode.DARK, manager.themeMode)
        assertEquals(SortOrder.DATE_DESC, manager.sortOrder)
    }

    @Test
    fun `theme type can be changed to gruvbox`() {
        val manager = SettingsManager(MapSettings())

        manager.themeType = ThemeType.GRUVBOX

        assertEquals(ThemeType.GRUVBOX, manager.themeType)
    }

    @Test
    fun `theme type can be changed back to catppuccin`() {
        val manager = SettingsManager(MapSettings())

        manager.themeType = ThemeType.GRUVBOX
        manager.themeType = ThemeType.CATPPUCCIN

        assertEquals(ThemeType.CATPPUCCIN, manager.themeType)
    }

    @Test
    fun `theme mode can be changed to light`() {
        val manager = SettingsManager(MapSettings())

        manager.themeMode = ThemeMode.LIGHT

        assertEquals(ThemeMode.LIGHT, manager.themeMode)
    }

    @Test
    fun `theme mode can be changed back to dark`() {
        val manager = SettingsManager(MapSettings())

        manager.themeMode = ThemeMode.LIGHT
        manager.themeMode = ThemeMode.DARK

        assertEquals(ThemeMode.DARK, manager.themeMode)
    }

    @Test
    fun `all sort orders can be persisted`() {
        val manager = SettingsManager(MapSettings())

        SortOrder.values().forEach { order ->
            manager.sortOrder = order
            assertEquals(order, manager.sortOrder)
        }
    }

    @Test
    fun `invalid sort order falls back to date desc`() {
        val settings = MapSettings()
        settings.putString("sort_order", "BROKEN_SORT")

        val manager = SettingsManager(settings)

        assertEquals(SortOrder.DATE_DESC, manager.sortOrder)
    }

    @Test
    fun `settings persist across manager instances`() {
        val settings = MapSettings()
        val first = SettingsManager(settings)

        first.themeType = ThemeType.GRUVBOX
        first.themeMode = ThemeMode.LIGHT
        first.sortOrder = SortOrder.TITLE_DESC

        val second = SettingsManager(settings)

        assertEquals(ThemeType.GRUVBOX, second.themeType)
        assertEquals(ThemeMode.LIGHT, second.themeMode)
        assertEquals(SortOrder.TITLE_DESC, second.sortOrder)
    }

    @Test
    fun `settings values are independent`() {
        val manager = SettingsManager(MapSettings())

        manager.themeType = ThemeType.GRUVBOX
        manager.themeMode = ThemeMode.LIGHT
        manager.sortOrder = SortOrder.TITLE_ASC

        assertEquals(ThemeType.GRUVBOX, manager.themeType)
        assertEquals(ThemeMode.LIGHT, manager.themeMode)
        assertEquals(SortOrder.TITLE_ASC, manager.sortOrder)
    }
}
