package com.example.myapplication.network

import com.example.myapplication.BuildConfig
import com.example.myapplication.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {

    fun create(sessionManager: SessionManager): BeautyTipsApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val authInterceptor = Interceptor { chain ->
            val token = sessionManager.fetchAuthToken()
            val request = chain.request()
            val authenticatedRequest = if (!token.isNullOrBlank() && request.header("Authorization") == null) {
                request.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                request
            }
            chain.proceed(authenticatedRequest)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://dkawbm-2a0b-4140-4f7b--2.ru.tuna.am/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(BeautyTipsApi::class.java)
    }
}
