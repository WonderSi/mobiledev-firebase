package com.example.habits.ui.createhabit

data class CreateHabitUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
