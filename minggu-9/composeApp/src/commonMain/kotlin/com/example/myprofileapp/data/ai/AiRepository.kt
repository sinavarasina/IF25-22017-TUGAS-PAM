package com.example.myprofileapp.data.ai

enum class AiAction(
    val title: String,
    val description: String,
) {
    SUMMARIZE(
        title = "Ringkas",
        description = "Buat ringkasan 3-5 poin dari teks catatan.",
    ),
    GENERATE_IDEAS(
        title = "Ide",
        description = "Generate 5 ide dari sebuah topik.",
    ),
    IMPROVE_WRITING(
        title = "Perbaiki",
        description = "Rapikan ejaan, struktur, dan gaya tulisan.",
    ),
    SUGGEST_TITLE(
        title = "Judul",
        description = "Buat satu judul singkat dari isi catatan.",
    ),
    TRANSLATE_TO_ENGLISH(
        title = "Translate EN",
        description = "Terjemahkan teks ke Bahasa Inggris natural.",
    ),
    CHAT(
        title = "Tanya AI",
        description = "Tanyakan hal umum ke AI assistant.",
    ),
}

class AiRepository(
    private val geminiService: GeminiService,
) {
    suspend fun execute(
        action: AiAction,
        input: String,
    ): Result<String> {
        val cleanInput = input.trim()
        if (cleanInput.isBlank()) {
            return Result.failure(IllegalArgumentException("Input tidak boleh kosong."))
        }

        return when (action) {
            AiAction.SUMMARIZE -> summarize(cleanInput)
            AiAction.GENERATE_IDEAS -> generateIdeas(cleanInput)
            AiAction.IMPROVE_WRITING -> improveWriting(cleanInput)
            AiAction.SUGGEST_TITLE -> suggestTitle(cleanInput)
            AiAction.TRANSLATE_TO_ENGLISH -> translateToEnglish(cleanInput)
            AiAction.CHAT -> chat(cleanInput)
        }
    }

    fun getPromptPreview(
        action: AiAction,
        input: String,
    ): String {
        val cleanInput = input.trim()
        return when (action) {
            AiAction.SUMMARIZE ->
                """
                Ringkas catatan berikut:

                $cleanInput
                """.trimIndent()

            AiAction.GENERATE_IDEAS ->
                """
                Berikan ide berdasarkan topik berikut:

                $cleanInput
                """.trimIndent()

            AiAction.IMPROVE_WRITING ->
                """
                Perbaiki tulisan berikut:

                $cleanInput
                """.trimIndent()

            AiAction.SUGGEST_TITLE ->
                """
                Buat judul untuk catatan berikut:

                $cleanInput
                """.trimIndent()

            AiAction.TRANSLATE_TO_ENGLISH ->
                """
                Terjemahkan teks berikut ke Bahasa Inggris:

                $cleanInput
                """.trimIndent()

            AiAction.CHAT -> cleanInput
        }
    }

    private suspend fun summarize(input: String): Result<String> =
        geminiService.generateContent(
            prompt = getPromptPreview(AiAction.SUMMARIZE, input),
            systemPrompt = SystemPrompts.SUMMARIZER,
        )

    private suspend fun generateIdeas(input: String): Result<String> =
        geminiService.generateContent(
            prompt = getPromptPreview(AiAction.GENERATE_IDEAS, input),
            systemPrompt = SystemPrompts.IDEA_GENERATOR,
        )

    private suspend fun improveWriting(input: String): Result<String> =
        geminiService.generateContent(
            prompt = getPromptPreview(AiAction.IMPROVE_WRITING, input),
            systemPrompt = SystemPrompts.WRITING_IMPROVER,
        )

    private suspend fun suggestTitle(input: String): Result<String> =
        geminiService.generateContent(
            prompt = getPromptPreview(AiAction.SUGGEST_TITLE, input),
            systemPrompt = SystemPrompts.TITLE_SUGGESTER,
        )

    private suspend fun translateToEnglish(input: String): Result<String> =
        geminiService.generateContent(
            prompt = getPromptPreview(AiAction.TRANSLATE_TO_ENGLISH, input),
            systemPrompt = SystemPrompts.TRANSLATOR,
        )

    private suspend fun chat(input: String): Result<String> =
        geminiService.generateContent(
            prompt = getPromptPreview(AiAction.CHAT, input),
            systemPrompt = SystemPrompts.CHAT_ASSISTANT,
        )
}
