package com.example.myprofileapp.viewmodel.notes

import androidx.lifecycle.ViewModel
import com.example.myprofileapp.data.notes.Note
import com.example.myprofileapp.data.notes.dummyNotes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotesViewModel : ViewModel() {
    private val _notes = MutableStateFlow(dummyNotes)
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    fun addNote(
        title: String,
        content: String,
    ) {
        val newNote =
            Note(
                id = (_notes.value.maxOfOrNull { it.id } ?: 0) + 1,
                title = title,
                content = content,
                isFavorite = false,
            )
        _notes.update { it + newNote }
    }

    fun updateNote(
        id: Int,
        title: String,
        content: String,
    ) {
        _notes.update { notes ->
            notes.map { if (it.id == id) it.copy(title = title, content = content) else it }
        }
    }

    fun toggleFavorite(id: Int) {
        _notes.update { notes ->
            notes.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it }
        }
    }

    fun deleteNote(id: Int) {
        _notes.update { notes -> notes.filter { it.id != id } }
    }

    fun getNoteById(id: Int): Note? = _notes.value.find { it.id == id }
}
