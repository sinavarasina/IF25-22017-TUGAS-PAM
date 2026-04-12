package com.example.myprofileapp.ui.screens.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofileapp.data.notes.dummyNotes
import com.example.myprofileapp.ui.components.notes.NoteCard
import com.example.myprofileapp.ui.components.profile.LabeledTextField
import com.example.myprofileapp.ui.theme.Colors

@Composable
fun NoteListScreen(colors: Colors, onNavigateToDetail: (Int) -> Unit, onNavigateToAdd: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(dummyNotes) { note ->
            NoteCard(
                note = note,
                colors = colors,
                onClick = { onNavigateToDetail(note.id) }
            )
        }
    }
}

@Composable
fun FavoritesScreen(colors: Colors, onNavigateToDetail: (Int) -> Unit) {
    val favoriteNotes = dummyNotes.filter { it.isFavorite }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(favoriteNotes) { note ->
            NoteCard(
                note = note,
                colors = colors,
                onClick = { onNavigateToDetail(note.id) }
            )
        }
    }
}

@Composable
fun NoteDetailScreen(
    noteId: Int,
    colors: Colors,
    onNavigateToEdit: (Int) -> Unit,
    onBack: () -> Unit
) {
    val note = dummyNotes.find { it.id == noteId }

    if (note != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = note.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = note.content, fontSize = 16.sp, color = colors.textSecondary)

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { onNavigateToEdit(noteId) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accentPrimary,
                    contentColor = colors.backgroundMain
                )
            ) {
                Text("Edit Note")
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Note not found!", color = colors.error)
        }
    }
}

@Composable
fun AddNoteScreen(colors: Colors) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        LabeledTextField(
            label = "Title",
            value = title,
            onValueChange = { title = it },
            colors = colors
        )
        LabeledTextField(
            label = "Content",
            value = content,
            onValueChange = { content = it },
            colors = colors
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* TODO: Logika simpan data */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.success,
                contentColor = colors.backgroundMain
            )
        ) {
            Text("Save Note")
        }
    }
}

@Composable
fun EditNoteScreen(noteId: Int, colors: Colors, onBack: () -> Unit) {
    val note = dummyNotes.find { it.id == noteId }

    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        LabeledTextField(
            label = "Title",
            value = title,
            onValueChange = { title = it },
            colors = colors
        )
        LabeledTextField(
            label = "Content",
            value = content,
            onValueChange = { content = it },
            colors = colors
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* TODO: Logika update data */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.accentSecondary,
                contentColor = colors.backgroundMain
            )
        ) {
            Text("Update Note")
        }
    }
}