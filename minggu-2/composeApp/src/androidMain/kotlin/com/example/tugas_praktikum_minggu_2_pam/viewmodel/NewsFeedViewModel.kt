package com.example.tugas_praktikum_minggu_2_pam.viewmodel

import com.example.tugas_praktikum_minggu_2_pam.data.NewsDataFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsFeedViewModel {
    private val dataSource = NewsDataFlow()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    private val _newsFeed = MutableStateFlow<List<String>>(emptyList())
    val newsFeed: StateFlow<List<String>> = _newsFeed.asStateFlow()

    fun startProcessing() {
        viewModelScope.launch {
            dataSource.getNewsStream()
                .catch { e -> println("Stream Error: ${e.message}") }
                .filter { news -> news.category == "Tech" || news.category == "Linux" || news.category == "Anime" }
                .map { news -> "[${news.category.uppercase()}] ${news.title}" }
                .collect { formattedNews ->
                    val detail = async(Dispatchers.IO) {
                        fetchNewsDetail(formattedNews)
                    }.await()
                    val content = "$formattedNews\n $detail"

                    _newsFeed.update { currentList -> listOf(content) + currentList }
                }
        }
    }

    fun markNewsAsRead() {
        _readCount.value++
    }

    private suspend fun fetchNewsDetail(title: String): String {
        delay(1000)
        return "Detail: News berhasil di fetch"
    }
}