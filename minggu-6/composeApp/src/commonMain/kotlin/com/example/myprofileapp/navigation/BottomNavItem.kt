package com.example.myprofileapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val title: String,
) {
    object News : BottomNavItem(Screen.NewsList.route, Icons.Default.Public, "News")

    object Notes : BottomNavItem(Screen.Notes.route, Icons.Default.List, "Notes")

    object Favorites : BottomNavItem(Screen.Favorites.route, Icons.Default.Favorite, "Favorites")

    object Profile : BottomNavItem(Screen.Profile.route, Icons.Default.Person, "Profile")
}
