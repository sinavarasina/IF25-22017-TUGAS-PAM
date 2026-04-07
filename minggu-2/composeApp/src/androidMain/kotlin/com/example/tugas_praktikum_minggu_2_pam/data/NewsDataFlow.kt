package com.example.tugas_praktikum_minggu_2_pam.data

import com.example.tugas_praktikum_minggu_2_pam.model.News
import com.example.tugas_praktikum_minggu_2_pam.model.NewsFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsDataFlow {
    fun getNewsStream(): Flow<News> = flow {
        var currentId = 1
        while (true) {
            delay(2000)
            val newNews = NewsFactory.generateNews(currentId)
            emit(newNews)
            currentId++
        }
    }
}