package com.example.habits.domain.repository

import com.example.habits.domain.model.HabitRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IHabitRecordRepository {
    fun getRecordsForHabit(habitId: Long): Flow<List<HabitRecord>>
    fun getRecordsForDate(date: LocalDate): Flow<List<HabitRecord>>
    suspend fun getRecordForDate(habitId: Long, date: LocalDate): HabitRecord?
    suspend fun insert(record: HabitRecord)
    suspend fun delete(id: Long)
}
