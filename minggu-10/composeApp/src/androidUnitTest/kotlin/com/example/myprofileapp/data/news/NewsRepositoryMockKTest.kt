package com.example.myprofileapp.data.news

import com.russhwolf.settings.MapSettings
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NewsRepositoryMockKTest {
    private lateinit var api: NewsApi
    private lateinit var settings: MapSettings
    private lateinit var repository: NewsRepository

    private val cacheKey = "男の子の秘密"
    private val firstArticle = Article(id = 1, userId = 10, title = "First", body = "Body one")
    private val secondArticle = Article(id = 2, userId = 20, title = "Second", body = "Body two")

    @BeforeTest
    fun setup() {
        api = mockk()
        settings = MapSettings()
        repository = NewsRepository(api = api, settings = settings)
    }

    @Test
    fun `getArticles returns fresh articles and caches them`() = runTest {
        coEvery { api.getArticles() } returns listOf(firstArticle, secondArticle)

        val result = repository.getArticles()

        assertTrue(result.isSuccess)
        assertEquals(listOf(firstArticle, secondArticle), result.getOrThrow())
        assertTrue(settings.getStringOrNull(cacheKey)?.contains("First") == true)
    }

    @Test
    fun `getArticles returns cached articles when api fails`() = runTest {
        settings.putString(cacheKey, Json.encodeToString(listOf(firstArticle)))
        coEvery { api.getArticles() } throws IllegalStateException("offline")

        val result = repository.getArticles()

        assertTrue(result.isSuccess)
        assertEquals(listOf(firstArticle), result.getOrThrow())
    }

    @Test
    fun `getArticles fails when api fails and cache is empty`() = runTest {
        coEvery { api.getArticles() } throws IllegalStateException("offline")

        val result = repository.getArticles()

        assertTrue(result.isFailure)
        assertEquals("No internet and no cached data.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getArticleById returns fresh article`() = runTest {
        coEvery { api.getArticleById(1) } returns firstArticle

        val result = repository.getArticleById(1)

        assertTrue(result.isSuccess)
        assertEquals(firstArticle, result.getOrThrow())
    }

    @Test
    fun `getArticleById returns cached article when api fails`() = runTest {
        settings.putString(cacheKey, Json.encodeToString(listOf(firstArticle, secondArticle)))
        coEvery { api.getArticleById(2) } throws IllegalStateException("offline")

        val result = repository.getArticleById(2)

        assertTrue(result.isSuccess)
        assertEquals(secondArticle, result.getOrThrow())
    }

    @Test
    fun `getArticleById fails when cached article is missing`() = runTest {
        settings.putString(cacheKey, Json.encodeToString(listOf(firstArticle)))
        coEvery { api.getArticleById(99) } throws IllegalStateException("offline")

        val result = repository.getArticleById(99)

        assertTrue(result.isFailure)
        assertEquals("Cached article not found.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getArticleById fails when api fails and cache is empty`() = runTest {
        coEvery { api.getArticleById(1) } throws IllegalStateException("offline")

        val result = repository.getArticleById(1)

        assertTrue(result.isFailure)
        assertEquals("No internet connection.", result.exceptionOrNull()?.message)
    }
}
