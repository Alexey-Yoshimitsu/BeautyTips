package com.example.myapplication.network

import com.example.myapplication.network.models.ArticleDto
import com.example.myapplication.network.models.ArticlePreviewDto
import com.example.myapplication.network.models.RegisterRequest
import com.example.myapplication.network.models.SupportMessageRequest
import com.example.myapplication.network.models.TokenResponse
import com.example.myapplication.network.models.UpdateProfileRequest
import com.example.myapplication.network.models.UserDto
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Field
import retrofit2.http.Query

interface BeautyTipsApi {

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): UserDto

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") email: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password",
        @Field("scope") scope: String = ""
    ): TokenResponse

    @GET("articles/")
    suspend fun getArticles(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 20
    ): List<ArticlePreviewDto>

    @GET("articles/random")
    suspend fun getRandomArticle(): ArticleDto

    @GET("users/me")
    suspend fun getProfile(): UserDto

    @PUT("users/me")
    suspend fun updateProfile(@Body body: UpdateProfileRequest): UserDto

    @POST("support/message")
    suspend fun sendSupportMessage(@Body body: SupportMessageRequest): Map<String, Any>
}
