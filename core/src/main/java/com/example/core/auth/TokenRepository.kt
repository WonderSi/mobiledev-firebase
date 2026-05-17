package com.example.core.auth

interface TokenRepository {
    fun saveToken(token: String, userName: String)
    fun getToken(): String?
    fun getUserName(): String?
    fun clearToken()
    fun isLoggedIn(): Boolean
}
