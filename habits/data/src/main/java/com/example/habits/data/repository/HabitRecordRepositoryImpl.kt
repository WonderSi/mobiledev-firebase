package com.example.habits.data.repository

import com.example.habits.data.local.dao.HabitRecordDao
import com.example.habits.data.mapper.toDomain
import com.example.habits.data.mapper.toEntity
import com.example.habits.domain.model.HabitRecord
import com.example.habits.domain.repository.IHabitRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class HabitRecordRepositoryImpl @Inject constructor(private val dao: HabitRecordDao) : IHabitRecordRepository {
    override fun getRecordsForHabit(habitId: Long): Flow<List<HabitRecord>> =
        dao.getRecordsForHabit(habitId).map { it.map { e -> e.toDomain() } }
    override fun getRecordsForDate(date: LocalDate): Flow<List<HabitRecord>> =
        dao.getRecordsForDate(date.toString()).map { it.map { e -> e.toDomain() } }
    override suspend fun getRecordForDate(habitId: Long, date: LocalDate): HabitRecord? =
        dao.getRecordForDate(habitId, date.toString())?.toDomain()
    override suspend fun insert(record: HabitRecord) = dao.insert(record.toEntity())
    override suspend fun delete(id: Long) = dao.delete(id)
}
