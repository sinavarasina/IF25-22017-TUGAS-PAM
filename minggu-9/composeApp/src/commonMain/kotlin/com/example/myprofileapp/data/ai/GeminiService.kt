package com.example.myprofileapp.data.ai

import com.example.myprofileapp.platform.ApiConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class GeminiService(
    private val client: HttpClient,
) {
    private companion object {
        const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta"
        private const val MODEL = "gemini-2.5-flash"
    }

    suspend fun generateContent(
        prompt: String,
        systemPrompt: String? = null,
    ): Result<String> =
        runCatching {
            val apiKey = ApiConfig.geminiApiKey
            require(apiKey.isNotBlank()) {
                "Gemini API key is not configured. Add GEMINI_API_KEY to local.properties."
            }

            val request =
                GeminiRequest(
                    contents = buildContents(prompt = prompt, systemPrompt = systemPrompt),
                    generationConfig =
                        GenerationConfig(
                            temperature = 0.7,
                            maxOutputTokens = 1000,
                        ),
                )

            val response: GeminiResponse =
                client
                    .post("$BASE_URL/models/$MODEL:generateContent") {
                        contentType(ContentType.Application.Json)
                        parameter("key", apiKey)
                        setBody(request)
                    }.body()

            response.getErrorMessage()?.let { errorMessage -> error(errorMessage) }
            response.getTextContent() ?: error("Respons kosong dari AI.")
        }

    private fun buildContents(
        prompt: String,
        systemPrompt: String?,
    ): List<GeminiContent> =
        buildList {
            if (!systemPrompt.isNullOrBlank()) {
                add(
                    GeminiContent(
                        role = "user",
                        parts = listOf(GeminiPart(systemPrompt)),
                    ),
                )
                add(
                    GeminiContent(
                        role = "model",
                        parts = listOf(GeminiPart("Baik, saya akan mengikuti instruksi tersebut.")),
                    ),
                )
            }

            add(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(prompt)),
                ),
            )
        }
}

object SystemPrompts {
    val SUMMARIZER =
        """
        Kamu adalah asisten yang ahli dalam merangkum catatan.
        Tugas: rangkum teks yang diberikan menjadi poin-poin utama yang singkat dan jelas.

        Rules:
        - Gunakan Bahasa Indonesia.
        - Berikan 3 sampai 5 poin utama.
        - Setiap poin maksimal 1 sampai 2 kalimat.
        - Fokus pada informasi paling penting.
        - Jangan menambahkan informasi yang tidak ada di teks asli.
        """.trimIndent()

    val IDEA_GENERATOR =
        """
        Kamu adalah asisten kreatif untuk brainstorming.
        Tugas: berikan ide berdasarkan topik yang diberikan.

        Rules:
        - Gunakan Bahasa Indonesia.
        - Berikan tepat 5 ide.
        - Setiap ide harus unik, praktis, dan bisa diimplementasikan.
        - Format sebagai daftar bernomor.
        """.trimIndent()

    val WRITING_IMPROVER =
        """
        Kamu adalah editor profesional.
        Tugas: perbaiki tulisan tanpa mengubah makna aslinya.

        Rules:
        - Gunakan Bahasa Indonesia yang rapi dan natural.
        - Perbaiki ejaan, struktur, dan kejelasan kalimat.
        - Pertahankan gaya asli penulis.
        - Jangan menambahkan informasi baru.
        - Berikan hanya hasil tulisan yang sudah diperbaiki.
        """.trimIndent()

    val TITLE_SUGGESTER =
        """
        Kamu adalah asisten untuk membuat judul catatan.
        Tugas: buat satu judul singkat dan menarik berdasarkan isi teks.

        Rules:
        - Gunakan Bahasa Indonesia.
        - Maksimal 7 kata.
        - Judul harus mencerminkan isi teks.
        - Berikan hanya judul, tanpa penjelasan dan tanpa tanda kutip.
        """.trimIndent()

    val TRANSLATOR =
        """
        Kamu adalah penerjemah profesional.
        Tugas: terjemahkan teks ke bahasa target dengan natural.

        Rules:
        - Pertahankan makna dan nuansa asli.
        - Jangan menerjemahkan terlalu literal jika terdengar kaku.
        - Berikan hanya hasil terjemahan.
        """.trimIndent()

    val CHAT_ASSISTANT =
        """
        Kamu adalah asisten AI di aplikasi catatan mahasiswa.
        Jawab dengan ringkas, jelas, dan membantu.
        Gunakan Bahasa Indonesia kecuali user meminta bahasa lain.
        """.trimIndent()
}
