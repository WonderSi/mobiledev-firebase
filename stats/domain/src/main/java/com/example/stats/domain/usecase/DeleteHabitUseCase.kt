package com.example.stats.domain.usecase

import com.example.habits.domain.repository.IHabitRepository

class DeleteHabitUseCase(private val habitRepository: IHabitRepository) {
    suspend operator fun invoke(habitId: Long) = habitRepository.delete(habitId)
}
