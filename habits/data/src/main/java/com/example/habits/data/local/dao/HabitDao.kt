package com.example.habits.data.local.dao

import androidx.room.*
import com.example.habits.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits")
    fun getAll(): Flow<List<HabitEntity>>
    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getById(id: Long): HabitEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity): Long
    @Update
    suspend fun update(habit: HabitEntity)
    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun delete(id: Long)
}
