package com.example.habits.data.di

import android.content.Context
import androidx.room.Room
import com.example.habits.data.local.HabitsDatabase
import com.example.habits.data.local.dao.HabitDao
import com.example.habits.data.local.dao.HabitRecordDao
import com.example.habits.data.repository.HabitRecordRepositoryImpl
import com.example.habits.data.repository.HabitRepositoryImpl
import com.example.habits.domain.repository.IHabitRecordRepository
import com.example.habits.domain.repository.IHabitRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HabitsDbModule {
    @Provides @Singleton
    fun provideHabitsDatabase(@ApplicationContext ctx: Context): HabitsDatabase =
        Room.databaseBuilder(ctx, HabitsDatabase::class.java, "habits.db").build()

    @Provides fun provideHabitDao(db: HabitsDatabase): HabitDao = db.habitDao()
    @Provides fun provideHabitRecordDao(db: HabitsDatabase): HabitRecordDao = db.habitRecordDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class HabitsRepositoryModule {
    @Binds abstract fun bindHabitRepository(impl: HabitRepositoryImpl): IHabitRepository
    @Binds abstract fun bindHabitRecordRepository(impl: HabitRecordRepositoryImpl): IHabitRecordRepository
}
