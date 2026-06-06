package com.example.myprofileapp.viewmodel.ai

import com.example.myprofileapp.data.ai.AiAction
import com.example.myprofileapp.data.ai.AiRepository
import com.example.myprofileapp.testutil.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AiViewModelMockKTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: AiRepository

    @BeforeTest
    fun setup() {
        repository = mockk(relaxed = true)
        every { repository.getPromptPreview(any(), any()) } answers {
            "preview:${firstArg<AiAction>().name}:${secondArg<String>()}"
        }
    }

    @Test
    fun `default ui state is summarize and empty`() {
        val viewModel = AiViewModel(repository)

        val state = viewModel.uiState.value
        assertEquals(AiAction.SUMMARIZE, state.selectedAction)
        assertEquals("", state.inputText)
        assertEquals("", state.resultText)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `setInputText updates input and prompt preview`() {
        val viewModel = AiViewModel(repository)

        viewModel.setInputText("hello")

        assertEquals("hello", viewModel.uiState.value.inputText)
        assertEquals("preview:SUMMARIZE:hello", viewModel.uiState.value.promptPreview)
    }

    @Test
    fun `setAction updates selected action and prompt preview`() {
        val viewModel = AiViewModel(repository)
        viewModel.setInputText("topic")

        viewModel.setAction(AiAction.GENERATE_IDEAS)

        assertEquals(AiAction.GENERATE_IDEAS, viewModel.uiState.value.selectedAction)
        assertEquals("preview:GENERATE_IDEAS:topic", viewModel.uiState.value.promptPreview)
    }

    @Test
    fun `execute ignores blank input`() = runTest {
        val viewModel = AiViewModel(repository)

        viewModel.execute()
        advanceUntilIdle()

        coVerify(exactly = 0) { repository.execute(any(), any()) }
        assertEquals("", viewModel.uiState.value.resultText)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `execute success stores result and clears loading`() = runTest {
        coEvery { repository.execute(AiAction.SUMMARIZE, "hello") } returns Result.success("result")
        val viewModel = AiViewModel(repository)
        viewModel.setInputText("hello")

        viewModel.execute()
        advanceUntilIdle()

        assertEquals("result", viewModel.uiState.value.resultText)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `execute failure stores error and clears loading`() = runTest {
        coEvery { repository.execute(AiAction.SUMMARIZE, "hello") } returns Result.failure(IllegalStateException("failed"))
        val viewModel = AiViewModel(repository)
        viewModel.setInputText("hello")

        viewModel.execute()
        advanceUntilIdle()

        assertEquals("failed", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("", viewModel.uiState.value.resultText)
    }

    @Test
    fun `clearResult clears result and error`() = runTest {
        coEvery { repository.execute(AiAction.SUMMARIZE, "hello") } returns Result.success("result")
        val viewModel = AiViewModel(repository)
        viewModel.setInputText("hello")
        viewModel.execute()
        advanceUntilIdle()

        viewModel.clearResult()

        assertEquals("", viewModel.uiState.value.resultText)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `clearAll resets state`() {
        val viewModel = AiViewModel(repository)
        viewModel.setInputText("hello")
        viewModel.setAction(AiAction.CHAT)

        viewModel.clearAll()

        assertEquals(AiUiState(), viewModel.uiState.value)
    }
}
