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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeType
import com.example.myprofileapp.navigation.Navigation
import com.example.myprofileapp.navigation.Screen
import com.example.myprofileapp.ui.components.AppBottomBar
import com.example.myprofileapp.ui.components.AppTopBar
import com.example.myprofileapp.ui.theme.Themes
import com.example.myprofileapp.viewmodel.news.NewsViewModel
import com.example.myprofileapp.viewmodel.notes.NotesViewModel
import com.example.myprofileapp.viewmodel.profile.ProfileViewModel
import com.example.myprofileapp.viewmodel.theme.ThemeViewModel

@Composable
@Preview
fun App() {
    val themeViewModel: ThemeViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val notesViewModel: NotesViewModel = viewModel()
    val newsViewModel: NewsViewModel = viewModel()
    val navController = rememberNavController()

    val themeState by themeViewModel.themeState.collectAsState()

    val currentTheme =
        when (themeState.activeThemeType) {
            ThemeType.CATPPUCCIN -> Themes.Catppuccin
            ThemeType.GRUVBOX -> Themes.GruvBox
        }
    val colors = if (themeState.themeMode == ThemeMode.DARK) currentTheme.dark else currentTheme.light

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomNav =
        currentDestination?.let {
            it.hasRoute<Screen.NewsList>() ||
                it.hasRoute<Screen.Notes>() ||
                it.hasRoute<Screen.Favorites>() ||
                it.hasRoute<Screen.Profile>()
        } == true

    val showFab = currentDestination?.hasRoute<Screen.Notes>() == true

    val topBarTitle =
        when {
            currentDestination?.hasRoute<Screen.Notes>() == true -> "Notes"
            currentDestination?.hasRoute<Screen.Favorites>() == true -> "Favorites"
            currentDestination?.hasRoute<Screen.Profile>() == true -> "Profile"
            currentDestination?.hasRoute<Screen.AddNote>() == true -> "Add Note"
            currentDestination?.hasRoute<Screen.NewsList>() == true -> "News"
            currentDestination?.hasRoute<Screen.NoteDetail>() == true -> "Note Detail"
            currentDestination?.hasRoute<Screen.EditNote>() == true -> "Edit Note"
            currentDestination?.hasRoute<Screen.NewsDetail>() == true -> "News Detail"
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
                    showThemeControls = currentDestination?.hasRoute<Screen.Profile>() == true,
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
                        onClick = { navController.navigate(Screen.AddNote) },
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
                notesViewModel = notesViewModel,
                newsViewModel = newsViewModel,
                colors = colors,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
