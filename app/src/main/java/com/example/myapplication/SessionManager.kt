package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .apply()
    }

    fun fetchAuthToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun setHasAccount(value: Boolean) {
        prefs.edit()
            .putBoolean(KEY_HAS_ACCOUNT, value)
            .apply()
    }

    fun hasAccount(): Boolean = prefs.getBoolean(KEY_HAS_ACCOUNT, false)

    fun isLoggedIn(): Boolean = !fetchAuthToken().isNullOrBlank()

    companion object {
        private const val PREFS_NAME = "beauty_tips_session"
        private const val KEY_TOKEN = "key_token"
        private const val KEY_HAS_ACCOUNT = "key_has_account"
    }
}
