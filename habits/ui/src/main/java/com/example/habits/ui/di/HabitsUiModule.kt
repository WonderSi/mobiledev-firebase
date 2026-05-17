package com.example.habits.ui.di

import com.example.habits.domain.repository.IHabitRecordRepository
import com.example.habits.domain.repository.IHabitRepository
import com.example.habits.domain.usecase.CreateHabitUseCase
import com.example.habits.domain.usecase.GetAllHabitsUseCase
import com.example.habits.domain.usecase.ToggleHabitCompletionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HabitsUiModule {

    @Provides
    fun provideGetAllHabitsUseCase(repo: IHabitRepository): GetAllHabitsUseCase =
        GetAllHabitsUseCase(repo)

    @Provides
    fun provideCreateHabitUseCase(repo: IHabitRepository): CreateHabitUseCase =
        CreateHabitUseCase(repo)

    @Provides
    fun provideToggleUseCase(
        habitRepo: IHabitRepository,
        recordRepo: IHabitRecordRepository
    ): ToggleHabitCompletionUseCase = ToggleHabitCompletionUseCase(habitRepo, recordRepo)
}
