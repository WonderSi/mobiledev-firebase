package com.example.habits.domain.model

import java.time.LocalDate

data class Habit(
    val id: Long = 0,
    val title: String,
    val description: String,
    val color: Int,
    val createdAt: LocalDate = LocalDate.now()
)
