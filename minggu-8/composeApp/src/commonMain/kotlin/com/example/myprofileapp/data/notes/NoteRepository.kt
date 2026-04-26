package com.example.myprofileapp.data.notes

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.myprofileapp.db.NotesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NoteRepository(
    private val database: NotesDatabase,
) {
    private val queries = database.noteQueries

    private fun com.example.myprofileapp.db.NoteEntity.toDomain() =
        Note(
            id = id.toInt(),
            title = title,
            content = content,
            isFavorite = is_favorite == 1L,
        )

    fun getAllNotes(): Flow<List<Note>> =
        queries
            .selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map { entity -> entity.toDomain() } }

    fun getAllNotesByTitle(): Flow<List<Note>> =
        queries
            .selectAllByTitle()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map { entity -> entity.toDomain() } }

    fun searchNotes(query: String): Flow<List<Note>> {
        val q = "%$query%"
        return queries
            .search(q, q)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map { entity -> entity.toDomain() } }
    }

    fun getAllNotesOldest(): Flow<List<Note>> =
        queries
            .selectAllOldest()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map { entity -> entity.toDomain() } }

    fun getAllNotesByTitleDesc(): Flow<List<Note>> =
        queries
            .selectAllByTitleDesc()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map { entity -> entity.toDomain() } }

    suspend fun getNoteById(id: Int): Note? =
        withContext(Dispatchers.IO) {
            queries.selectById(id.toLong()).executeAsOneOrNull()?.toDomain()
        }

    suspend fun insertNote(
        title: String,
        content: String,
    ) {
        val now = System.currentTimeMillis()
        withContext(Dispatchers.IO) {
            queries.insert(title, content, now, now)
        }
    }

    suspend fun updateNote(
        id: Int,
        title: String,
        content: String,
    ) {
        val now = System.currentTimeMillis()
        withContext(Dispatchers.IO) {
            queries.update(title, content, now, id.toLong())
        }
    }

    suspend fun toggleFavorite(
        id: Int,
        isFavorite: Boolean,
    ) {
        val now = System.currentTimeMillis()
        withContext(Dispatchers.IO) {
            queries.toggleFavorite(
                is_favorite = if (isFavorite) 0L else 1L,
                updated_at = now,
                id = id.toLong(),
            )
        }
    }

    suspend fun deleteNote(id: Int) {
        withContext(Dispatchers.IO) {
            queries.delete(id.toLong())
        }
    }
}
