package com.example.myprofileapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myprofileapp.ui.screens.notes.AddNoteScreen
import com.example.myprofileapp.ui.screens.notes.EditNoteScreen
import com.example.myprofileapp.ui.screens.notes.FavoritesScreen
import com.example.myprofileapp.ui.screens.notes.NoteDetailScreen
import com.example.myprofileapp.ui.screens.notes.NoteListScreen
import com.example.myprofileapp.ui.screens.profile.ProfileScreen
import com.example.myprofileapp.ui.theme.Colors
import com.example.myprofileapp.viewmodel.profile.ProfileViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    colors: Colors,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route,
        modifier = modifier
    ) {
        composable(Screen.Notes.route) {
            NoteListScreen(colors = colors, onNavigateToDetail = { id ->
                navController.navigate(Screen.NoteDetail.createRoute(id))
            })
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(colors = colors, onNavigateToDetail = { id ->
                navController.navigate(Screen.NoteDetail.createRoute(id))
            })
        }
        composable(Screen.Profile.route) {
            ProfileScreen(profileViewModel = profileViewModel, colors = colors)
        }

        composable(Screen.AddNote.route) {
            AddNoteScreen(colors = colors)
        }
        composable(
            route = Screen.NoteDetail.route,
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.let { bundle ->
                NavType.IntType.get(bundle, "noteId")
            } ?: 0

            NoteDetailScreen(noteId = id, colors = colors, onNavigateToEdit = { noteId ->
                navController.navigate(Screen.EditNote.createRoute(noteId))
            })
        }
        composable(
            route = Screen.EditNote.route,
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.let { bundle ->
                NavType.IntType.get(bundle, "noteId")
            } ?: 0

            EditNoteScreen(noteId = id, colors = colors)
        }
    }
}