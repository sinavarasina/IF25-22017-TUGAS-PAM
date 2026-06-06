package com.example.myprofileapp.viewmodel.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofileapp.data.notes.Note
import com.example.myprofileapp.data.notes.NoteRepository
import com.example.myprofileapp.data.settings.SettingsManager
import com.example.myprofileapp.data.settings.SortOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NoteRepository,
    private val settingsManager: SettingsManager,
    coroutineScope: CoroutineScope? = null,
) : ViewModel() {
    private val scope = coroutineScope ?: viewModelScope
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(settingsManager.sortOrder)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    val notes: StateFlow<List<Note>> =
        combine(_searchQuery, _sortOrder) { query, sort -> Pair(query, sort) }
            .flatMapLatest { (query, sort) ->
                if (query.isNotBlank()) {
                    repository.searchNotes(query)
                } else {
                    when (sort) {
                        SortOrder.TITLE_ASC -> repository.getAllNotesByTitle()
                        SortOrder.TITLE_DESC -> repository.getAllNotesByTitleDesc()
                        SortOrder.DATE_DESC -> repository.getAllNotes()
                        SortOrder.DATE_ASC -> repository.getAllNotesOldest()
                    }
                }
            }.stateIn(scope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
        settingsManager.sortOrder = order
    }

    fun addNote(
        title: String,
        content: String,
    ) = scope.launch { repository.insertNote(title, content) }

    fun updateNote(
        id: Int,
        title: String,
        content: String,
    ) = scope.launch { repository.updateNote(id, title, content) }

    fun toggleFavorite(id: Int) =
        scope.launch {
            val note = notes.value.find { it.id == id } ?: return@launch
            repository.toggleFavorite(id, note.isFavorite)
        }

    fun deleteNote(id: Int) = scope.launch { repository.deleteNote(id) }

    fun getNoteById(id: Int): Note? = notes.value.find { it.id == id }
}
