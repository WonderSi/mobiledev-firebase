package com.example.mobiledev_firebase.auth

import com.example.core.auth.AuthService
import com.example.core.auth.TokenRepository
import com.example.core.auth.model.AuthProvider
import com.example.core.auth.model.User

class AuthServiceImpl(
    private val tokenRepository: TokenRepository
) : AuthService {

    private var currentUser: User? = null

    override fun saveVkUser(accessToken: String, userId: Long, email: String?): User {
        val user = User(
            id = userId.toString(),
            name = email ?: "VK User",
            email = email,
            avatarUrl = null,
            provider = AuthProvider.VK
        )
        currentUser = user
        tokenRepository.saveToken(accessToken, user.name)
        return user
    }

    override fun saveYandexUser(accessToken: String): User {
        val user = User(
            id = "yandex",
            name = "Yandex User",
            email = null,
            avatarUrl = null,
            provider = AuthProvider.YANDEX
        )
        currentUser = user
        tokenRepository.saveToken(accessToken, user.name)
        return user
    }

    override fun logout() {
        currentUser = null
        tokenRepository.clearToken()
    }

    override fun getCurrentUser(): User? {
        if (currentUser != null) return currentUser
        val name = tokenRepository.getUserName() ?: return null
        return User(id = "", name = name, email = null, avatarUrl = null, provider = AuthProvider.VK)
    }
}
