package com.example.habits.ui.habitlist

data class HabitListUiState(
    val items: List<HabitListItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
