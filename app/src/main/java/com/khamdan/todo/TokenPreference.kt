package com.khamdan.todo

import android.content.Context
import android.content.SharedPreferences

class TokenPreference(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private val TOKEN_KEY = "TOKEN_KEY"

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        val token = sharedPreferences.getString(TOKEN_KEY, null)
        return if (!token.isNullOrBlank()) "Bearer $token" else null
    }

    companion object {
        private const val PREFERENCES_NAME = "TokenPreferences"
    }
}
