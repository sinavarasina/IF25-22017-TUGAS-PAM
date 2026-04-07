package com.example.tugas_praktikum_minggu_2_pam.model

data class News(
    val id: Int,
    val title: String,
    val category: String
)

object NewsFactory {
    private val dataPairs = listOf(
        Pair("Crunchyroll salah upload episode anime fate strange fake", "Anime"),
        Pair("Whatsapp web dikabarkan akan menambahkan call support di website", "Tech"),
        Pair("Rust di linux kernel sudah permanen, tag experimental sudah dihapus", "Linux"),
        Pair("Linux 6.19 keluar dengan dukungan driver modern AMDGPU pada gpu legacy", "Linux"),
        Pair("Pipewire 1.6 rilis dengan banyak fitur baru", "Linux"),
        Pair("SIMDJson menunjukan peningkatan performa pada SIMD di JSON Parsing", "Tech"),
        Pair("Apple M3 terus menunjukan peningkatan pada Asahi Linux", "Linux"),
        Pair("Linux 7.0 membawa beberapa peningkatan untuk laptop modern", "Linux")
    )

    fun generateNews(id: Int): News {
        val randomNews = dataPairs.random()
        val (title, category) = randomNews
        return News(
            id = id,
            title = title,
            category = category
        )
    }
}
