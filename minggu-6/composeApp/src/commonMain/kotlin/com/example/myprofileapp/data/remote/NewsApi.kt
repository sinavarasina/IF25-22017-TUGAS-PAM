package com.example.myprofileapp.data.remote

import com.example.myprofileapp.data.model.Article
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class NewsApi(
    private val client: HttpClient,
) {
    private val baseUrl = "https://jsonplaceholder.typicode.com"

    suspend fun getArticles(): List<Article> = client.get("$baseUrl/posts").body()

    suspend fun getArticleById(id: Int): Article = client.get("$baseUrl/posts/$id").body()
}
