package com.example.habits.data.mapper

import com.example.habits.data.local.entity.HabitEntity
import com.example.habits.domain.model.Habit
import java.time.LocalDate

fun HabitEntity.toDomain() = Habit(id = id, title = title, description = description, color = color, createdAt = LocalDate.parse(createdAt))
fun Habit.toEntity() = HabitEntity(id = id, title = title, description = description, color = color, createdAt = createdAt.toString())
