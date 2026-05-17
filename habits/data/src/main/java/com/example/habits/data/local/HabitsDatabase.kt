package com.example.habits.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.habits.data.local.dao.HabitDao
import com.example.habits.data.local.dao.HabitRecordDao
import com.example.habits.data.local.entity.HabitEntity
import com.example.habits.data.local.entity.HabitRecordEntity

@Database(entities = [HabitEntity::class, HabitRecordEntity::class], version = 1)
abstract class HabitsDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitRecordDao(): HabitRecordDao
}
