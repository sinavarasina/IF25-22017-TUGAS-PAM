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
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myprofileapp.data.news.HttpClientFactory
import com.example.myprofileapp.data.news.NewsApi
import com.example.myprofileapp.data.news.NewsRepository
import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeType
import com.example.myprofileapp.navigation.Navigation
import com.example.myprofileapp.navigation.Screen
import com.example.myprofileapp.ui.components.AppBottomBar
import com.example.myprofileapp.ui.components.AppTopBar
import com.example.myprofileapp.ui.rememberAppNavigationState
import com.example.myprofileapp.ui.theme.Themes
import com.example.myprofileapp.viewmodel.news.NewsViewModel
import com.example.myprofileapp.viewmodel.notes.NotesViewModel
import com.example.myprofileapp.viewmodel.profile.ProfileViewModel
import com.example.myprofileapp.viewmodel.theme.ThemeViewModel
import com.russhwolf.settings.Settings

@Composable
@Preview
fun App() {
    val themeViewModel: ThemeViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val notesViewModel: NotesViewModel = viewModel()

    val newsViewModel: NewsViewModel =
        viewModel {
            val repository =
                NewsRepository(
                    api = NewsApi(HttpClientFactory.create()),
                    settings = Settings(),
                )
            NewsViewModel(repository)
        }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val appState = rememberAppNavigationState(navBackStackEntry?.destination)

    val themeState by themeViewModel.themeState.collectAsState()
    val isDark = themeState.themeMode == ThemeMode.DARK
    val currentTheme =
        when (themeState.activeThemeType) {
            ThemeType.CATPPUCCIN -> Themes.Catppuccin
            ThemeType.GRUVBOX -> Themes.GruvBox
        }
    val colors = if (isDark) currentTheme.dark else currentTheme.light

    val colorScheme = if (isDark) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            topBar = {
                AppTopBar(
                    title = appState.topBarTitle,
                    colors = colors,
                    activeThemeType = themeState.activeThemeType,
                    themeMode = themeState.themeMode,
                    onThemeTypeChange = { themeViewModel.setThemeType(it) },
                    onThemeModeChange = { themeViewModel.setThemeMode(it) },
                    showThemeControls = appState.showThemeControls,
                )
            },
            bottomBar = {
                if (appState.showBottomNav) {
                    AppBottomBar(navController = navController, colors = colors)
                }
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = appState.showFab,
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
