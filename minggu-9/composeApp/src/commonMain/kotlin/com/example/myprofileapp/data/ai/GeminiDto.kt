package com.example.myprofileapp.data.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GenerationConfig = GenerationConfig(),
)

@Serializable
data class GeminiContent(
    val parts: List<GeminiPart>,
    val role: String,
)

@Serializable
data class GeminiPart(
    val text: String,
)

@Serializable
data class GenerationConfig(
    val temperature: Double = 0.7,
    @SerialName("maxOutputTokens") val maxOutputTokens: Int = 1000,
    @SerialName("topP") val topP: Double = 0.95,
)

@Serializable
data class GeminiResponse(
    val candidates: List<GeminiCandidate> = emptyList(),
    val promptFeedback: PromptFeedback? = null,
    val error: GeminiError? = null,
)

@Serializable
data class GeminiCandidate(
    val content: GeminiContent? = null,
    val finishReason: String? = null,
)

@Serializable
data class PromptFeedback(
    val blockReason: String? = null,
)

@Serializable
data class GeminiError(
    val code: Int? = null,
    val message: String? = null,
    val status: String? = null,
)

fun GeminiResponse.getTextContent(): String? =
    candidates
        .firstOrNull()
        ?.content
        ?.parts
        ?.firstOrNull()
        ?.text
        ?.trim()
        ?.takeIf { it.isNotBlank() }

fun GeminiResponse.getErrorMessage(): String? {
    error?.message?.let { return it }

    val blockReason = promptFeedback?.blockReason
    if (!blockReason.isNullOrBlank()) {
        return "Prompt blocked by Gemini: $blockReason"
    }

    val finishReason = candidates.firstOrNull()?.finishReason
    if (finishReason == "SAFETY" || finishReason == "RECITATION") {
        return "Gemini stopped the response: $finishReason"
    }

    return null
}
