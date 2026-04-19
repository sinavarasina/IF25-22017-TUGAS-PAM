package com.example.myprofileapp.data.repository

import com.example.myprofileapp.data.model.Article
import com.example.myprofileapp.data.remote.NewsApi
import com.russhwolf.settings.Settings
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NewsRepository(
    private val api: NewsApi,
) {
    private val settings = Settings()
    private val CACHE_KEY = "男の子の秘密"

    suspend fun getArticles(): Result<List<Article>> =
        try {
            val freshArticles = api.getArticles()

            val jsonString = Json.encodeToString(freshArticles)
            settings.putString(CACHE_KEY, jsonString)

            Result.success(freshArticles)
        } catch (e: Exception) {
            val cachedJson = settings.getStringOrNull(CACHE_KEY)

            if (cachedJson != null) {
                val cachedArticles = Json.decodeFromString<List<Article>>(cachedJson)
                Result.success(cachedArticles)
            } else {
                Result.failure(Exception("No internet and no cached data."))
            }
        }

    suspend fun getArticleById(id: Int): Result<Article> =
        try {
            Result.success(api.getArticleById(id))
        } catch (e: Exception) {
            val cachedJson = settings.getStringOrNull(CACHE_KEY)
            if (cachedJson != null) {
                val cachedArticles = Json.decodeFromString<List<Article>>(cachedJson)
                val article = cachedArticles.find { it.id == id }

                if (article != null) {
                    Result.success(article)
                } else {
                    Result.failure(Exception("Cached article not found."))
                }
            } else {
                Result.failure(Exception("No internet connection."))
            }
        }
}
