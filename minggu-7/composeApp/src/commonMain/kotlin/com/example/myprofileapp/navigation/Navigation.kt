package com.example.myprofileapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.myprofileapp.data.settings.SettingsManager
import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeType
import com.example.myprofileapp.ui.screens.news.NewsDetailScreen
import com.example.myprofileapp.ui.screens.news.NewsListScreen
import com.example.myprofileapp.ui.screens.notes.AddNoteScreen
import com.example.myprofileapp.ui.screens.notes.EditNoteScreen
import com.example.myprofileapp.ui.screens.notes.FavoritesScreen
import com.example.myprofileapp.ui.screens.notes.NoteDetailScreen
import com.example.myprofileapp.ui.screens.notes.NoteListScreen
import com.example.myprofileapp.ui.screens.profile.ProfileScreen
import com.example.myprofileapp.ui.screens.settings.SettingsScreen
import com.example.myprofileapp.ui.theme.Colors
import com.example.myprofileapp.viewmodel.news.NewsViewModel
import com.example.myprofileapp.viewmodel.notes.NotesViewModel
import com.example.myprofileapp.viewmodel.profile.ProfileViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    notesViewModel: NotesViewModel,
    newsViewModel: NewsViewModel,
    settingsManager: SettingsManager,
    onThemeTypeChange: (ThemeType) -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
    colors: Colors,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NewsList,
        modifier = modifier,
    ) {
        composable<Screen.NewsList> {
            NewsListScreen(
                viewModel = newsViewModel,
                colors = colors,
                onNavigateToDetail = { id -> navController.navigate(Screen.NewsDetail(id)) },
            )
        }

        composable<Screen.NewsDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.NewsDetail>()
            NewsDetailScreen(articleId = args.articleId, viewModel = newsViewModel, colors = colors)
        }

        composable<Screen.Notes> {
            NoteListScreen(
                colors = colors,
                notesViewModel = notesViewModel,
                onNavigateToDetail = { id -> navController.navigate(Screen.NoteDetail(id)) },
                onNavigateToAdd = { navController.navigate(Screen.AddNote) },
            )
        }

        composable<Screen.Favorites> {
            FavoritesScreen(
                colors = colors,
                notesViewModel = notesViewModel,
                onNavigateToDetail = { id -> navController.navigate(Screen.NoteDetail(id)) },
            )
        }

        composable<Screen.Profile> {
            ProfileScreen(profileViewModel = profileViewModel, colors = colors)
        }

        composable<Screen.AddNote> {
            AddNoteScreen(
                colors = colors,
                notesViewModel = notesViewModel,
                onBack = { navController.popBackStack() },
            )
        }

        composable<Screen.NoteDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.NoteDetail>()
            NoteDetailScreen(
                noteId = args.noteId,
                colors = colors,
                notesViewModel = notesViewModel,
                onNavigateToEdit = { id -> navController.navigate(Screen.EditNote(id)) },
                onBack = { navController.popBackStack() },
            )
        }

        composable<Screen.EditNote> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EditNote>()
            EditNoteScreen(
                noteId = args.noteId,
                colors = colors,
                notesViewModel = notesViewModel,
                onBack = { navController.popBackStack() },
            )
        }
        composable<Screen.Settings> {
            SettingsScreen(
                settingsManager = settingsManager,
                colors = colors,
                onThemeTypeChange = onThemeTypeChange,
                onThemeModeChange = onThemeModeChange,
            )
        }
    }
}
