package com.example.myprofileapp.data.notes

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val isFavorite: Boolean
)

val dummyNotes = listOf(

    Note(
        id = 1,
        title = "Watchlist (Weekly)",
        content = """
            - Re:Zero kara Hajimeru Isekai Seikatsu S4
            - Classroom of the Elite S4
            - Dr. Stone: Science Future Part 3
            - Tensei shitara Slime Datta Ken S4
            
            Catatan:
            - Wajib nonton tiap minggu
            - Hindari spoiler
        """.trimIndent(),
        isFavorite = true,
    ),

    Note(
        id = 2,
        title = "Menunggu Batch",
        content = """
            - Witch Hat Atelier
            - Yomi no Tsugai
            - Otonari no Tenshi-sama S2
            
            Alasan:
            - Lebih enak no distraction
            - Pacing santai / world building berat
            
            Status:
            - Tunggu tamat dulu baru gas
        """.trimIndent(),
        isFavorite = true,
    ),

    Note(
        id = 3,
        title = "Avoid List",
        content = """
            - Anime generic isekai (kalau plot NPC level)
            - Adaptasi rushed
            - Yang rating & review jelek
            
            Catatan:
            - Waktu terbatas
            - Trust instinct > hype
        """.trimIndent(),
        isFavorite = false,
    )
)