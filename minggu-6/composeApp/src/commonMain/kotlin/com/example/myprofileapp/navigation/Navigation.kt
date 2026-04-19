package com.example.myprofileapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myprofileapp.ui.screens.news.NewsDetailScreen
import com.example.myprofileapp.ui.screens.news.NewsListScreen
import com.example.myprofileapp.ui.screens.notes.AddNoteScreen
import com.example.myprofileapp.ui.screens.notes.EditNoteScreen
import com.example.myprofileapp.ui.screens.notes.FavoritesScreen
import com.example.myprofileapp.ui.screens.notes.NoteDetailScreen
import com.example.myprofileapp.ui.screens.notes.NoteListScreen
import com.example.myprofileapp.ui.screens.profile.ProfileScreen
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
    colors: Colors,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NewsList.route,
        modifier = modifier,
    ) {
        composable(Screen.NewsList.route) {
            NewsListScreen(
                viewModel = newsViewModel,
                colors = colors,
                onNavigateToDetail = { id -> navController.navigate(Screen.NewsDetail.createRoute(id)) },
            )
        }
        composable(
            route = Screen.NewsDetail.route,
            arguments = listOf(navArgument("articleId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.let { NavType.IntType.get(it, "articleId") } ?: 0
            NewsDetailScreen(articleId = id, viewModel = newsViewModel, colors = colors)
        }
        composable(Screen.Notes.route) {
            NoteListScreen(
                colors = colors,
                notesViewModel = notesViewModel,
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.NoteDetail.createRoute(id))
                },
                onNavigateToAdd = {
                    navController.navigate(Screen.AddNote.route)
                },
            )
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                colors = colors,
                notesViewModel = notesViewModel,
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.NoteDetail.createRoute(id))
                },
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(profileViewModel = profileViewModel, colors = colors)
        }
        composable(Screen.AddNote.route) {
            AddNoteScreen(
                colors = colors,
                notesViewModel = notesViewModel,
                onBack = { navController.popBackStack() },
            )
        }
        composable(
            route = Screen.NoteDetail.route,
            arguments = listOf(navArgument("noteId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val id =
                backStackEntry.arguments?.let { bundle ->
                    NavType.IntType.get(bundle, "noteId")
                } ?: 0
            NoteDetailScreen(
                noteId = id,
                colors = colors,
                notesViewModel = notesViewModel,
                onNavigateToEdit = { noteId ->
                    navController.navigate(Screen.EditNote.createRoute(noteId))
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable(
            route = Screen.EditNote.route,
            arguments = listOf(navArgument("noteId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val id =
                backStackEntry.arguments?.let { bundle ->
                    NavType.IntType.get(bundle, "noteId")
                } ?: 0
            EditNoteScreen(
                noteId = id,
                colors = colors,
                notesViewModel = notesViewModel,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
