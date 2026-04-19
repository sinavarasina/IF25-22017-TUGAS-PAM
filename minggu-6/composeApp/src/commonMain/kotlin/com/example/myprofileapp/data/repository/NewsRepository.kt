package com.example.myprofileapp.data.repository

import com.example.myprofileapp.data.model.Article
import com.example.myprofileapp.data.remote.NewsApi

class NewsRepository(
    private val api: NewsApi,
) {
    suspend fun getArticles(): Result<List<Article>> =
        try {
            Result.success(api.getArticles())
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun getArticleById(id: Int): Result<Article> =
        try {
            Result.success(api.getArticleById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
}
