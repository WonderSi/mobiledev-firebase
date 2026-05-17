package com.example.habits.data.local.dao

import androidx.room.*
import com.example.habits.data.local.entity.HabitRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitRecordDao {
    @Query("SELECT * FROM habit_records WHERE habitId = :habitId")
    fun getRecordsForHabit(habitId: Long): Flow<List<HabitRecordEntity>>
    @Query("SELECT * FROM habit_records WHERE habitId = :habitId AND completedAt = :date LIMIT 1")
    suspend fun getRecordForDate(habitId: Long, date: String): HabitRecordEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: HabitRecordEntity)
    @Query("SELECT * FROM habit_records WHERE completedAt = :date")
    fun getRecordsForDate(date: String): Flow<List<HabitRecordEntity>>
    @Query("DELETE FROM habit_records WHERE id = :id")
    suspend fun delete(id: Long)
}
