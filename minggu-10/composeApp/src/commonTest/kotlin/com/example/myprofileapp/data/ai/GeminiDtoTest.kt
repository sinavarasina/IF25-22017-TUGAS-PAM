package com.example.myprofileapp.data.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GeminiDtoTest {
    @Test
    fun `getTextContent returns trimmed text from first candidate`() {
        val response =
            GeminiResponse(
                candidates =
                    listOf(
                        GeminiCandidate(
                            content = GeminiContent(role = "model", parts = listOf(GeminiPart("  Hello AI  "))),
                        ),
                    ),
            )

        assertEquals("Hello AI", response.getTextContent())
    }

    @Test
    fun `getTextContent ignores empty candidates`() {
        val response = GeminiResponse(candidates = emptyList())

        assertNull(response.getTextContent())
    }

    @Test
    fun `getTextContent ignores null content`() {
        val response = GeminiResponse(candidates = listOf(GeminiCandidate(content = null)))

        assertNull(response.getTextContent())
    }

    @Test
    fun `getTextContent ignores empty parts`() {
        val response =
            GeminiResponse(
                candidates =
                    listOf(
                        GeminiCandidate(
                            content = GeminiContent(role = "model", parts = emptyList()),
                        ),
                    ),
            )

        assertNull(response.getTextContent())
    }

    @Test
    fun `getTextContent ignores blank text`() {
        val response =
            GeminiResponse(
                candidates =
                    listOf(
                        GeminiCandidate(
                            content = GeminiContent(role = "model", parts = listOf(GeminiPart("   "))),
                        ),
                    ),
            )

        assertNull(response.getTextContent())
    }

    @Test
    fun `getErrorMessage returns api error first`() {
        val response = GeminiResponse(error = GeminiError(code = 400, message = "Bad request", status = "INVALID"))

        assertEquals("Bad request", response.getErrorMessage())
    }

    @Test
    fun `getErrorMessage returns prompt blocked message`() {
        val response = GeminiResponse(promptFeedback = PromptFeedback(blockReason = "SAFETY"))

        assertEquals("Prompt blocked by Gemini: SAFETY", response.getErrorMessage())
    }

    @Test
    fun `getErrorMessage returns safety finish reason`() {
        val response = GeminiResponse(candidates = listOf(GeminiCandidate(finishReason = "SAFETY")))

        assertEquals("Gemini stopped the response: SAFETY", response.getErrorMessage())
    }

    @Test
    fun `getErrorMessage returns recitation finish reason`() {
        val response = GeminiResponse(candidates = listOf(GeminiCandidate(finishReason = "RECITATION")))

        assertEquals("Gemini stopped the response: RECITATION", response.getErrorMessage())
    }

    @Test
    fun `getErrorMessage returns null for normal response`() {
        val response =
            GeminiResponse(
                candidates =
                    listOf(
                        GeminiCandidate(
                            content = GeminiContent(role = "model", parts = listOf(GeminiPart("OK"))),
                            finishReason = "STOP",
                        ),
                    ),
            )

        assertNull(response.getErrorMessage())
    }

    @Test
    fun `generation config default values are stable`() {
        val config = GenerationConfig()

        assertEquals(0.7, config.temperature)
        assertEquals(1000, config.maxOutputTokens)
        assertEquals(0.95, config.topP)
    }

    @Test
    fun `gemini request stores content and config`() {
        val content = GeminiContent(role = "user", parts = listOf(GeminiPart("Prompt")))
        val config = GenerationConfig(temperature = 0.1, maxOutputTokens = 128, topP = 0.5)

        val request = GeminiRequest(contents = listOf(content), generationConfig = config)

        assertEquals(1, request.contents.size)
        assertEquals("user", request.contents.first().role)
        assertEquals("Prompt", request.contents.first().parts.first().text)
        assertEquals(128, request.generationConfig.maxOutputTokens)
    }
}
