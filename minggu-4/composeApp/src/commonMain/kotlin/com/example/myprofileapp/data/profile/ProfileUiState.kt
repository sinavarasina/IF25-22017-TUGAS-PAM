package com.example.myprofileapp.data.profile

import com.example.myprofileapp.ui.colors.Theme
import com.example.myprofileapp.ui.colors.Themes

data class ProfileUiState(
    val name: String = "Varasina Farmadani",
    val bio: String = "a regular human, nothing special about me.",
    val studentId: String = "123140107",
    val email: String = "sina@sinanonym.my.id",
    val phone: String = "+6285117788740",
    val website: String = "sinanonym.my.id",

    val isOnline: Boolean = true,
    val showDetailInfo: Boolean = false,
    val isEditing: Boolean = false,
    
    val currentTheme: Theme = Themes.Catppuccin,
    val isDarkMode: Boolean = true
)