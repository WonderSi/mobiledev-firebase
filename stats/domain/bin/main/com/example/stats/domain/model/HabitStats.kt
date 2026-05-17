package com.example.stats.domain.model

import com.example.habits.domain.model.Habit

data class HabitStats(
    val habit: Habit,
    val currentStreak: Int,
    val longestStreak: Int,
    val totalCompleted: Int
)
