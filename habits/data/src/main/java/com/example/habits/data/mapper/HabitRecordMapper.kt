package com.example.habits.data.mapper

import com.example.habits.data.local.entity.HabitRecordEntity
import com.example.habits.domain.model.HabitRecord
import java.time.LocalDate

fun HabitRecordEntity.toDomain() = HabitRecord(id = id, habitId = habitId, completedAt = LocalDate.parse(completedAt))
fun HabitRecord.toEntity() = HabitRecordEntity(id = id, habitId = habitId, completedAt = completedAt.toString())
