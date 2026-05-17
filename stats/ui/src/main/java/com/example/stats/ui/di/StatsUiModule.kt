package com.example.stats.ui.di

import com.example.habits.domain.repository.IHabitRecordRepository
import com.example.habits.domain.repository.IHabitRepository
import com.example.stats.domain.usecase.DeleteHabitUseCase
import com.example.stats.domain.usecase.GetHabitStatsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object StatsUiModule {

    @Provides
    fun provideGetHabitStatsUseCase(
        habitRepo: IHabitRepository,
        recordRepo: IHabitRecordRepository
    ): GetHabitStatsUseCase = GetHabitStatsUseCase(habitRepo, recordRepo)

    @Provides
    fun provideDeleteHabitUseCase(repo: IHabitRepository): DeleteHabitUseCase =
        DeleteHabitUseCase(repo)
}
