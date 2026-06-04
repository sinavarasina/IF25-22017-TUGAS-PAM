package com.example.myprofileapp.ui.screens.notes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myprofileapp.data.settings.SettingsManager
import com.example.myprofileapp.testutil.FakeNoteRepository
import com.example.myprofileapp.testutil.testNote
import com.example.myprofileapp.ui.test.NotesTestTags
import com.example.myprofileapp.ui.theme.Themes
import com.example.myprofileapp.viewmodel.notes.NotesViewModel
import com.russhwolf.settings.Settings
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteListScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun emptyNotes_showsEmptyState() {
        setScreen(repository = FakeNoteRepository())
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(NotesTestTags.EmptyState).assertIsDisplayed()
        composeRule.onNodeWithText("No Notes.").assertIsDisplayed()
    }

    @Test
    fun notesList_showsNoteTitleAndContent() {
        setScreen(
            repository =
                FakeNoteRepository(
                    listOf(testNote(title = "KMP Testing", content = "Compose UI test")),
                ),
        )
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(NotesTestTags.NotesList).assertIsDisplayed()
        composeRule.onNodeWithText("KMP Testing").assertIsDisplayed()
        composeRule.onNodeWithText("Compose UI test").assertIsDisplayed()
    }

    @Test
    fun sortButton_isDisplayed() {
        setScreen(repository = FakeNoteRepository())
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(NotesTestTags.SortButton).assertIsDisplayed()
        composeRule.onNodeWithText("Sort: Newest").assertIsDisplayed()
    }

    private fun setScreen(repository: FakeNoteRepository) {
        val viewModel =
            NotesViewModel(
                repository = repository,
                settingsManager = SettingsManager(Settings()),
            )

        composeRule.setContent {
            NoteListScreen(
                colors = Themes.Catppuccin.dark,
                notesViewModel = viewModel,
                onNavigateToDetail = {},
                onNavigateToAdd = {},
            )
        }
    }
}
