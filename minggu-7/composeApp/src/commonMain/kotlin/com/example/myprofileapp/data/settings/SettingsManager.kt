package com.example.myprofileapp.data.settings

import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeType
import com.russhwolf.settings.Settings

enum class SortOrder { BY_DATE, BY_TITLE }

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
            SortOrder.valueOf(
                settings.getString("sort_order", SortOrder.BY_DATE.name),
            )
        set(value) {
            settings.putString("sort_order", value.name)
        }
}
