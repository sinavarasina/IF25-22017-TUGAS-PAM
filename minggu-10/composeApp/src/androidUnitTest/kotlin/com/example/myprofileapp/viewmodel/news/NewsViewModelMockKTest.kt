package com.example.myprofileapp.viewmodel.news

import com.example.myprofileapp.data.common.UiState
import com.example.myprofileapp.data.news.Article
import com.example.myprofileapp.data.news.NewsRepository
import com.example.myprofileapp.testutil.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelMockKTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val article = Article(id = 1, userId = 2, title = "Title", body = "Body")

    @Test
    fun `init loads articles successfully`() = runTest {
        val repository = mockk<NewsRepository>()
        coEvery { repository.getArticles() } returns Result.success(listOf(article))

        val viewModel = NewsViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.articlesState.value
        assertIs<UiState.Success<List<Article>>>(state)
        assertEquals(listOf(article), state.data)
    }

    @Test
    fun `init stores error when loading articles fails`() = runTest {
        val repository = mockk<NewsRepository>()
        coEvery { repository.getArticles() } returns Result.failure(IllegalStateException("offline"))

        val viewModel = NewsViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.articlesState.value
        assertIs<UiState.Error>(state)
        assertEquals("offline", state.message)
    }

    @Test
    fun `refresh loads articles and clears refreshing flag`() = runTest {
        val repository = mockk<NewsRepository>()
        coEvery { repository.getArticles() } returns Result.success(listOf(article))

        val viewModel = NewsViewModel(repository)
        advanceUntilIdle()

        viewModel.refresh()
        advanceUntilIdle()

        val state = viewModel.articlesState.value
        assertIs<UiState.Success<List<Article>>>(state)
        assertEquals(listOf(article), state.data)
        assertFalse(viewModel.isRefreshing.value)
    }

    @Test
    fun `refresh stores error and clears refreshing flag`() = runTest {
        val repository = mockk<NewsRepository>()
        coEvery { repository.getArticles() } returns Result.failure(IllegalStateException("offline"))

        val viewModel = NewsViewModel(repository)
        advanceUntilIdle()

        viewModel.refresh()
        advanceUntilIdle()

        val state = viewModel.articlesState.value
        assertIs<UiState.Error>(state)
        assertEquals("offline", state.message)
        assertFalse(viewModel.isRefreshing.value)
    }

    @Test
    fun `loadArticleDetail stores success state`() = runTest {
        val repository = mockk<NewsRepository>()
        coEvery { repository.getArticles() } returns Result.success(emptyList())
        coEvery { repository.getArticleById(1) } returns Result.success(article)

        val viewModel = NewsViewModel(repository)
        advanceUntilIdle()

        viewModel.loadArticleDetail(1)
        advanceUntilIdle()

        val state = viewModel.detailState.value
        assertIs<UiState.Success<Article>>(state)
        assertEquals(article, state.data)
    }

    @Test
    fun `loadArticleDetail stores error state`() = runTest {
        val repository = mockk<NewsRepository>()
        coEvery { repository.getArticles() } returns Result.success(emptyList())
        coEvery { repository.getArticleById(1) } returns Result.failure(IllegalStateException("not found"))

        val viewModel = NewsViewModel(repository)
        advanceUntilIdle()

        viewModel.loadArticleDetail(1)
        advanceUntilIdle()

        val state = viewModel.detailState.value
        assertIs<UiState.Error>(state)
        assertEquals("not found", state.message)
    }
}
