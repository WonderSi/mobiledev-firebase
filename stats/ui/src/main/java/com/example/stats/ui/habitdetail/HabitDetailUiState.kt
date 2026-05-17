package com.example.stats.ui.habitdetail

import com.example.stats.domain.model.HabitStats

data class HabitDetailUiState(
    val stats: HabitStats? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
