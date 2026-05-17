package com.example.core.auth

import com.example.core.auth.model.User

interface AuthService {
    fun saveVkUser(accessToken: String, userId: Long, email: String?): User
    fun saveYandexUser(accessToken: String): User
    fun logout()
    fun getCurrentUser(): User?
}
