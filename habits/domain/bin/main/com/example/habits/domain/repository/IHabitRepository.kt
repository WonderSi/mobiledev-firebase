package com.example.habits.domain.repository

import com.example.habits.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface IHabitRepository {
    fun getAll(): Flow<List<Habit>>
    suspend fun getById(id: Long): Habit?
    suspend fun insert(habit: Habit): Long
    suspend fun update(habit: Habit)
    suspend fun delete(id: Long)
}
