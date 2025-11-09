package com.example.myapplication.network.models

import com.squareup.moshi.Json

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class TokenResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String
)

data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    @Json(name = "avatar_url") val avatarUrl: String?,
    @Json(name = "created_at") val createdAt: String
)

data class ArticlePreviewDto(
    val id: Int,
    val title: String,
    val preview: String,
    @Json(name = "read_time") val readTime: Int,
    @Json(name = "is_popular") val isPopular: Boolean,
    @Json(name = "created_at") val createdAt: String
)

data class ArticleDto(
    val id: Int,
    val title: String,
    val preview: String,
    val content: String,
    @Json(name = "read_time") val readTime: Int,
    @Json(name = "is_popular") val isPopular: Boolean,
    @Json(name = "created_at") val createdAt: String
)

data class UpdateProfileRequest(
    val name: String?,
    val email: String?
)

data class SupportMessageRequest(
    val subject: String,
    val message: String
)
