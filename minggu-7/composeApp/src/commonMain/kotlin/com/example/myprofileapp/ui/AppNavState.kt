package com.example.myprofileapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.example.myprofileapp.navigation.Screen

data class AppNavState(
    val showBottomNav: Boolean,
    val showFab: Boolean,
    val topBarTitle: String,
    val showThemeControls: Boolean,
)

@Composable
fun rememberAppNavigationState(currentDestination: NavDestination?): AppNavState =
    remember(currentDestination) {
        AppNavState(
            showBottomNav =
                currentDestination?.let {
                    it.hasRoute<Screen.NewsList>() ||
                        it.hasRoute<Screen.Notes>() ||
                        it.hasRoute<Screen.Favorites>() ||
                        it.hasRoute<Screen.Profile>()
                } == true,
            showFab = currentDestination?.hasRoute<Screen.Notes>() == true,
            showThemeControls = currentDestination?.hasRoute<Screen.Profile>() == true,
            topBarTitle =
                when {
                    currentDestination?.hasRoute<Screen.NewsList>() == true -> "News"
                    currentDestination?.hasRoute<Screen.Notes>() == true -> "Notes"
                    currentDestination?.hasRoute<Screen.Favorites>() == true -> "Favorites"
                    currentDestination?.hasRoute<Screen.Profile>() == true -> "Profile"
                    currentDestination?.hasRoute<Screen.AddNote>() == true -> "Add Note"
                    currentDestination?.hasRoute<Screen.NoteDetail>() == true -> "Note Detail"
                    currentDestination?.hasRoute<Screen.EditNote>() == true -> "Edit Note"
                    currentDestination?.hasRoute<Screen.NewsDetail>() == true -> "News Detail"
                    else -> "MyApp"
                },
        )
    }
