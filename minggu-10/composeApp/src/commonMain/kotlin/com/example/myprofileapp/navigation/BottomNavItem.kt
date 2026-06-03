package com.example.myprofileapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: Any,
    val icon: ImageVector,
    val title: String,
) {
    object News : BottomNavItem(Screen.NewsList, Icons.Default.Public, "News")

    object Notes : BottomNavItem(Screen.Notes, Icons.Default.List, "Notes")

    object Favorites : BottomNavItem(Screen.Favorites, Icons.Default.Favorite, "Favorites")

    object Profile : BottomNavItem(Screen.Profile, Icons.Default.Person, "Profile")

    object Settings : BottomNavItem(Screen.Settings, Icons.Default.Settings, "Settings")
}
