package com.example.myapplication

data class ArticleUiModel(
    val id: Int,
    val title: String,
    val preview: String,
    val timeAgo: String,
    val isPopular: Boolean
)
