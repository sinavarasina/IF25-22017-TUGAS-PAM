package com.example.myprofileapp.data.ai

import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AiRepositoryMockKTest {
    private lateinit var service: GeminiService
    private lateinit var repository: AiRepository

    @BeforeTest
    fun setup() {
        service = mockk(relaxed = true)
        repository = AiRepository(service)
    }

    @Test
    fun `blank input returns failure and does not call service`() = runTest {
        val result = repository.execute(AiAction.SUMMARIZE, "   ")

        assertTrue(result.isFailure)
        assertEquals("Input tidak boleh kosong.", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { service.generateContent(any(), any()) }
    }

    @Test
    fun `summarize action calls service with summarizer prompt`() = runTest {
        assertActionUsesPrompt(
            action = AiAction.SUMMARIZE,
            expectedPromptText = "Ringkas catatan berikut:",
            expectedSystemPrompt = SystemPrompts.SUMMARIZER,
        )
    }

    @Test
    fun `generate ideas action calls service with idea prompt`() = runTest {
        assertActionUsesPrompt(
            action = AiAction.GENERATE_IDEAS,
            expectedPromptText = "Berikan ide berdasarkan topik berikut:",
            expectedSystemPrompt = SystemPrompts.IDEA_GENERATOR,
        )
    }

    @Test
    fun `improve writing action calls service with writing prompt`() = runTest {
        assertActionUsesPrompt(
            action = AiAction.IMPROVE_WRITING,
            expectedPromptText = "Perbaiki tulisan berikut:",
            expectedSystemPrompt = SystemPrompts.WRITING_IMPROVER,
        )
    }

    @Test
    fun `suggest title action calls service with title prompt`() = runTest {
        assertActionUsesPrompt(
            action = AiAction.SUGGEST_TITLE,
            expectedPromptText = "Buat judul untuk catatan berikut:",
            expectedSystemPrompt = SystemPrompts.TITLE_SUGGESTER,
        )
    }

    @Test
    fun `translate action calls service with translation prompt`() = runTest {
        assertActionUsesPrompt(
            action = AiAction.TRANSLATE_TO_ENGLISH,
            expectedPromptText = "Terjemahkan teks berikut ke Bahasa Inggris:",
            expectedSystemPrompt = SystemPrompts.TRANSLATOR,
        )
    }

    @Test
    fun `chat action sends raw input with chat system prompt`() = runTest {
        coEvery { service.generateContent(any(), any()) } returns Result.success("reply")

        val result = repository.execute(AiAction.CHAT, "  Halo AI  ")

        assertEquals("reply", result.getOrThrow())
        coVerify(exactly = 1) {
            service.generateContent(
                prompt = "Halo AI",
                systemPrompt = SystemPrompts.CHAT_ASSISTANT,
            )
        }
    }

    @Test
    fun `service failure is propagated`() = runTest {
        coEvery { service.generateContent(any(), any()) } returns Result.failure(IllegalStateException("API down"))

        val result = repository.execute(AiAction.SUMMARIZE, "Input")

        assertTrue(result.isFailure)
        assertEquals("API down", result.exceptionOrNull()?.message)
    }

    private suspend fun assertActionUsesPrompt(
        action: AiAction,
        expectedPromptText: String,
        expectedSystemPrompt: String,
    ) {
        clearMocks(service)
        coEvery { service.generateContent(any(), any()) } returns Result.success("ok")

        val result = repository.execute(action, "  Isi catatan  ")

        assertEquals("ok", result.getOrThrow())
        coVerify(exactly = 1) {
            service.generateContent(
                prompt = match { it.contains(expectedPromptText) && it.contains("Isi catatan") },
                systemPrompt = expectedSystemPrompt,
            )
        }
    }
}
