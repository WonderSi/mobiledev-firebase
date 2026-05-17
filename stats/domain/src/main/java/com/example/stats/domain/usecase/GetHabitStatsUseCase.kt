package com.example.stats.domain.usecase

import com.example.habits.domain.repository.IHabitRecordRepository
import com.example.habits.domain.repository.IHabitRepository
import com.example.stats.domain.model.HabitStats
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class GetHabitStatsUseCase(
    private val habitRepository: IHabitRepository,
    private val recordRepository: IHabitRecordRepository
) {
    suspend operator fun invoke(habitId: Long): HabitStats {
        val habit = habitRepository.getById(habitId)
            ?: throw IllegalArgumentException("Habit not found: $habitId")
        val records = recordRepository.getRecordsForHabit(habitId).first()
        val dates = records.map { it.completedAt }.toSortedSet()
        val totalCompleted = dates.size

        var currentStreak = 0
        var date = LocalDate.now()
        while (dates.contains(date)) { currentStreak++; date = date.minusDays(1) }

        var longestStreak = 0; var tempStreak = 0; var prevDate: LocalDate? = null
        for (d in dates) {
            tempStreak = if (prevDate == null || d == prevDate.plusDays(1)) tempStreak + 1 else 1
            longestStreak = maxOf(longestStreak, tempStreak)
            prevDate = d
        }
        return HabitStats(habit, currentStreak, longestStreak, totalCompleted)
    }
}
