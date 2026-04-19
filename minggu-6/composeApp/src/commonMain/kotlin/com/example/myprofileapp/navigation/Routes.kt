package com.example.myprofileapp.navigation

sealed class Screen(
    val route: String,
) {
    object NewsList : Screen("news_list")

    object Notes : Screen("notes_list")

    object Favorites : Screen("favorites")

    object Profile : Screen("profile")

    object AddNote : Screen("add_note")

    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }

    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }

    object NewsDetail : Screen("news_detail/{articleId}") {
        fun createRoute(id: Int) = "news_detail/$id"
    }
}
