package com.example.myprofileapp.ui.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofileapp.data.settings.SortOrder
import com.example.myprofileapp.ui.components.notes.NoteCard
import com.example.myprofileapp.ui.components.profile.LabeledTextField
import com.example.myprofileapp.ui.theme.Colors
import com.example.myprofileapp.viewmodel.notes.NotesViewModel

@Composable
fun NoteListScreen(
    colors: Colors,
    notesViewModel: NotesViewModel,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToAdd: () -> Unit,
) {
    val notes by notesViewModel.notes.collectAsState()
    val searchQuery by notesViewModel.searchQuery.collectAsState()
    val sortOrder by notesViewModel.sortOrder.collectAsState()

    var isSortMenuExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            Box {
                TextButton(onClick = { isSortMenuExpanded = true }) {
                    val sortText =
                        when (sortOrder) {
                            SortOrder.DATE_DESC -> "Newest"
                            SortOrder.DATE_ASC -> "Oldest"
                            SortOrder.TITLE_ASC -> "A-Z"
                            SortOrder.TITLE_DESC -> "Z-A"
                        }
                    Text(text = "Sort: $sortText", color = colors.accentPrimary)
                }

                DropdownMenu(
                    expanded = isSortMenuExpanded,
                    onDismissRequest = { isSortMenuExpanded = false },
                    modifier = Modifier.background(colors.backgroundCard),
                ) {
                    SortOrder.entries.forEach { order ->
                        DropdownMenuItem(
                            text = {
                                val label =
                                    when (order) {
                                        SortOrder.DATE_DESC -> "Newest First"
                                        SortOrder.DATE_ASC -> "Oldest First"
                                        SortOrder.TITLE_ASC -> "Title (A-Z)"
                                        SortOrder.TITLE_DESC -> "Title (Z-A)"
                                    }
                                Text(
                                    text = label,
                                    color = colors.textPrimary,
                                    fontWeight = if (order == sortOrder) FontWeight.Bold else FontWeight.Normal,
                                )
                            },
                            onClick = {
                                notesViewModel.setSortOrder(order)
                                isSortMenuExpanded = false
                            },
                        )
                    }
                }
            }
        }

        if (notes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = if (searchQuery.isNotBlank()) "No Result for \"$searchQuery\"" else "No Notes.",
                    color = colors.textSecondary,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        colors = colors,
                        onClick = { onNavigateToDetail(note.id) },
                        onFavoriteClick = { notesViewModel.toggleFavorite(note.id) },
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen(
    colors: Colors,
    notesViewModel: NotesViewModel,
    onNavigateToDetail: (Int) -> Unit,
) {
    val notes by notesViewModel.notes.collectAsState()
    val favoriteNotes = notes.filter { it.isFavorite }

    if (favoriteNotes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No favorites yet.", color = colors.textSecondary)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(favoriteNotes, key = { it.id }) { note ->
                NoteCard(
                    note = note,
                    colors = colors,
                    onClick = { onNavigateToDetail(note.id) },
                    onFavoriteClick = { notesViewModel.toggleFavorite(note.id) },
                )
            }
        }
    }
}

@Composable
fun NoteDetailScreen(
    noteId: Int,
    colors: Colors,
    notesViewModel: NotesViewModel,
    onNavigateToEdit: (Int) -> Unit,
    onBack: () -> Unit,
) {
    val notes by notesViewModel.notes.collectAsState()
    val note = notes.find { it.id == noteId }

    if (note != null) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = note.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                fontSize = 16.sp,
                color = colors.textSecondary,
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { notesViewModel.toggleFavorite(noteId) },
                modifier = Modifier.fillMaxWidth(),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = if (note.isFavorite) colors.error else colors.success,
                        contentColor = colors.backgroundMain,
                    ),
            ) {
                Text(if (note.isFavorite) "Remove from Favorites" else "Add to Favorites")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onNavigateToEdit(noteId) },
                modifier = Modifier.fillMaxWidth(),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = colors.accentPrimary,
                        contentColor = colors.backgroundMain,
                    ),
            ) {
                Text("Edit Note")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    notesViewModel.deleteNote(noteId)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Delete Note", color = colors.error)
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Note not found!", color = colors.error)
        }
    }
}

@Composable
fun AddNoteScreen(
    colors: Colors,
    notesViewModel: NotesViewModel,
    onBack: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        LabeledTextField(
            label = "Title",
            value = title,
            onValueChange = {
                title = it
                showError = false
            },
            colors = colors,
        )

        if (showError) {
            Text(
                text = "Title cant be empty",
                color = colors.error,
                fontSize = 12.sp,
            )
        }

        LabeledTextField(
            label = "Content",
            value = content,
            onValueChange = { content = it },
            colors = colors,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (title.isBlank()) {
                    showError = true
                } else {
                    notesViewModel.addNote(title, content)
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = colors.success,
                    contentColor = colors.backgroundMain,
                ),
        ) {
            Text("Save Note")
        }
    }
}

@Composable
fun EditNoteScreen(
    noteId: Int,
    colors: Colors,
    notesViewModel: NotesViewModel,
    onBack: () -> Unit,
) {
    val note = notesViewModel.getNoteById(noteId)

    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        LabeledTextField(
            label = "Title",
            value = title,
            onValueChange = {
                title = it
                showError = false
            },
            colors = colors,
        )

        if (showError) {
            Text(
                text = "Title cant be empty",
                color = colors.error,
                fontSize = 12.sp,
            )
        }

        LabeledTextField(
            label = "Content",
            value = content,
            onValueChange = { content = it },
            colors = colors,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (title.isBlank()) {
                    showError = true
                } else {
                    notesViewModel.updateNote(noteId, title, content)
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = colors.accentSecondary,
                    contentColor = colors.backgroundMain,
                ),
        ) {
            Text("Update Note")
        }
    }
}
