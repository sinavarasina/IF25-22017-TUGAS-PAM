package com.example.myprofileapp.testutil

import com.example.myprofileapp.data.notes.Note
import com.example.myprofileapp.data.notes.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeNoteRepository(
    initialNotes: List<Note> = emptyList(),
) : NoteRepository {
    private val notes = MutableStateFlow(initialNotes)
    private var nextId = (initialNotes.maxOfOrNull { it.id } ?: 0) + 1

    fun snapshot(): List<Note> = notes.value

    override fun getAllNotes(): Flow<List<Note>> = notes

    override fun getAllNotesByTitle(): Flow<List<Note>> =
        notes.map { list -> list.sortedBy { it.title.lowercase() } }

    override fun searchNotes(query: String): Flow<List<Note>> =
        notes.map { list ->
            val cleanQuery = query.trim()
            if (cleanQuery.isBlank()) {
                list
            } else {
                list.filter { note ->
                    note.title.contains(cleanQuery, ignoreCase = true) ||
                        note.content.contains(cleanQuery, ignoreCase = true)
                }
            }
        }

    override fun getAllNotesOldest(): Flow<List<Note>> = notes

    override fun getAllNotesByTitleDesc(): Flow<List<Note>> =
        notes.map { list -> list.sortedByDescending { it.title.lowercase() } }

    override suspend fun getNoteById(id: Int): Note? = notes.value.find { it.id == id }

    override suspend fun insertNote(
        title: String,
        content: String,
    ) {
        val note =
            Note(
                id = nextId++,
                title = title,
                content = content,
                isFavorite = false,
            )
        notes.value = notes.value + note
    }

    override suspend fun updateNote(
        id: Int,
        title: String,
        content: String,
    ) {
        notes.value =
            notes.value.map { note ->
                if (note.id == id) note.copy(title = title, content = content) else note
            }
    }

    override suspend fun toggleFavorite(
        id: Int,
        isFavorite: Boolean,
    ) {
        notes.value =
            notes.value.map { note ->
                if (note.id == id) note.copy(isFavorite = !isFavorite) else note
            }
    }

    override suspend fun deleteNote(id: Int) {
        notes.value = notes.value.filterNot { it.id == id }
    }
}

fun testNote(
    id: Int = 1,
    title: String = "Test Note",
    content: String = "Test content",
    isFavorite: Boolean = false,
): Note =
    Note(
        id = id,
        title = title,
        content = content,
        isFavorite = isFavorite,
    )
