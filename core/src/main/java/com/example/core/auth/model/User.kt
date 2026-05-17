package com.example.core.auth.model

data class User(
    val id: String,
    val name: String,
    val email: String?,
    val avatarUrl: String?,
    val provider: AuthProvider
)
