package com.example.myprofileapp.viewmodel.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofileapp.data.common.UiState
import com.example.myprofileapp.data.model.Article
import com.example.myprofileapp.data.remote.HttpClientFactory
import com.example.myprofileapp.data.remote.NewsApi
import com.example.myprofileapp.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(
    private val repository: NewsRepository,
) : ViewModel() {
    private val _articlesState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)
    val articlesState: StateFlow<UiState<List<Article>>> = _articlesState.asStateFlow()

    private val _detailState = MutableStateFlow<UiState<Article>>(UiState.Loading)
    val detailState: StateFlow<UiState<Article>> = _detailState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            _articlesState.value = UiState.Loading
            repository
                .getArticles()
                .onSuccess { _articlesState.value = UiState.Success(it) }
                .onFailure { _articlesState.value = UiState.Error(it.message ?: "Unknown error") }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository
                .getArticles()
                .onSuccess { _articlesState.value = UiState.Success(it) }
                .onFailure { _articlesState.value = UiState.Error(it.message ?: "Unknown error") }
            _isRefreshing.value = false
        }
    }

    fun loadArticleDetail(id: Int) {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            repository
                .getArticleById(id)
                .onSuccess { _detailState.value = UiState.Success(it) }
                .onFailure { _detailState.value = UiState.Error(it.message ?: "Unknown error") }
        }
    }
}
