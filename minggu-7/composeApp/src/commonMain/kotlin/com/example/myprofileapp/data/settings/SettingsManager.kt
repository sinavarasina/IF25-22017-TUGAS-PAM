package com.example.myprofileapp.data.settings

import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeType
import com.russhwolf.settings.Settings

enum class SortOrder { DATE_DESC, DATE_ASC, TITLE_ASC, TITLE_DESC }

class SettingsManager(
    private val settings: Settings,
) {
    var themeType: ThemeType
        get() =
            ThemeType.valueOf(
                settings.getString("theme_type", ThemeType.CATPPUCCIN.name),
            )
        set(value) {
            settings.putString("theme_type", value.name)
        }

    var themeMode: ThemeMode
        get() =
            ThemeMode.valueOf(
                settings.getString("theme_mode", ThemeMode.DARK.name),
            )
        set(value) {
            settings.putString("theme_mode", value.name)
        }
    var sortOrder: SortOrder
        get() =
            try {
                SortOrder.valueOf(
                    settings.getString("sort_order", SortOrder.DATE_DESC.name),
                )
            } catch (e: Exception) {
                SortOrder.DATE_DESC
            }
        set(value) {
            settings.putString("sort_order", value.name)
        }
}
