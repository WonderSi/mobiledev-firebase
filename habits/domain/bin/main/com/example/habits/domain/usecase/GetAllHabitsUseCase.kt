package com.example.habits.domain.usecase

import com.example.habits.domain.model.Habit
import com.example.habits.domain.repository.IHabitRepository
import kotlinx.coroutines.flow.Flow

class GetAllHabitsUseCase(private val habitRepository: IHabitRepository) {
    operator fun invoke(): Flow<List<Habit>> = habitRepository.getAll()
}
