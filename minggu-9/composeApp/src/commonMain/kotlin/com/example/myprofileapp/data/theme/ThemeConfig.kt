package com.example.myprofileapp.data.theme

enum class ThemeType {
    CATPPUCCIN,
    GRUVBOX,
}

enum class ThemeMode {
    LIGHT,
    DARK,
}

data class ThemeState(
    val activeThemeType: ThemeType = ThemeType.CATPPUCCIN,
    val themeMode: ThemeMode = ThemeMode.DARK,
)
