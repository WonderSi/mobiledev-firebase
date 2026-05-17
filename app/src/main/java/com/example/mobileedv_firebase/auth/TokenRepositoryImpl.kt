package com.example.mobileedv_firebase.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.core.auth.TokenRepository

class TokenRepositoryImpl(context: Context) : TokenRepository {

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_auth_prefs",
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun saveToken(token: String, userName: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USER_NAME, userName)
            .apply()
    }

    override fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    override fun getUserName(): String? = prefs.getString(KEY_USER_NAME, null)

    override fun clearToken() {
        prefs.edit().clear().apply()
    }

    override fun isLoggedIn(): Boolean = getToken() != null

    companion object {
        private const val KEY_TOKEN = "access_token"
        private const val KEY_USER_NAME = "user_name"
    }
}
