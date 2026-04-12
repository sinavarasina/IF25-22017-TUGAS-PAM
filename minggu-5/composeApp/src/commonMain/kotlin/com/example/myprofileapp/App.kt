package com.example.myprofileapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeType
import com.example.myprofileapp.navigation.Navigation
import com.example.myprofileapp.navigation.Screen
import com.example.myprofileapp.ui.components.AppBottomBar
import com.example.myprofileapp.ui.components.AppTopBar
import com.example.myprofileapp.ui.theme.Themes
import com.example.myprofileapp.viewmodel.profile.ProfileViewModel
import com.example.myprofileapp.viewmodel.theme.ThemeViewModel

@Composable
@Preview
fun App() {
    val themeViewModel: ThemeViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val navController = rememberNavController()

    val themeState by themeViewModel.themeState.collectAsState()

    val currentTheme =
        when (themeState.activeThemeType) {
            ThemeType.CATPPUCCIN -> Themes.Catppuccin
            ThemeType.GRUVBOX -> Themes.GruvBox
        }
    val colors = if (themeState.themeMode == ThemeMode.DARK) currentTheme.dark else currentTheme.light

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes =
        listOf(
            Screen.Notes.route,
            Screen.Favorites.route,
            Screen.Profile.route,
        )
    val showBottomNav = currentRoute in bottomNavRoutes
    val showFab = currentRoute == Screen.Notes.route

    val topBarTitle =
        when {
            currentRoute == Screen.Notes.route -> "Notes"
            currentRoute == Screen.Favorites.route -> "Favorites"
            currentRoute == Screen.Profile.route -> "Profile"
            currentRoute == Screen.AddNote.route -> "Add Note"
            currentRoute?.startsWith("note_detail") == true -> "Note Detail"
            currentRoute?.startsWith("edit_note") == true -> "Edit Note"
            else -> "MyApp"
        }

    MaterialTheme {
        Scaffold(
            topBar = {
                AppTopBar(
                    title = topBarTitle,
                    colors = colors,
                    activeThemeType = themeState.activeThemeType,
                    themeMode = themeState.themeMode,
                    onThemeTypeChange = { themeViewModel.setThemeType(it) },
                    onThemeModeChange = { themeViewModel.setThemeMode(it) },
                    showThemeControls = currentRoute == Screen.Profile.route,
                )
            },
            bottomBar = {
                if (showBottomNav) {
                    AppBottomBar(navController = navController, colors = colors)
                }
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = showFab,
                    enter = scaleIn(),
                    exit = scaleOut(),
                ) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Screen.AddNote.route) },
                        containerColor = colors.accentPrimary,
                        contentColor = colors.backgroundMain,
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Note")
                    }
                }
            },
            containerColor = colors.backgroundMain,
        ) { innerPadding ->
            Navigation(
                navController = navController,
                profileViewModel = profileViewModel,
                colors = colors,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
