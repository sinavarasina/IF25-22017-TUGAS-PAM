package com.example.myprofileapp.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable data object NewsList : Screen

    @Serializable data object Notes : Screen

    @Serializable data object Favorites : Screen

    @Serializable data object Profile : Screen

    @Serializable data object AddNote : Screen

    @Serializable data class NoteDetail(
        val noteId: Int,
    ) : Screen

    @Serializable data class EditNote(
        val noteId: Int,
    ) : Screen

    @Serializable data class NewsDetail(
        val articleId: Int,
    ) : Screen
}
