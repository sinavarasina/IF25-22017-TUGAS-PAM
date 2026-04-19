package com.example.myprofileapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)
