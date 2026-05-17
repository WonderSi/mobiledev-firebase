package com.example.habits.ui.habitlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.analytics.AnalyticsService
import com.example.habits.domain.repository.IHabitRecordRepository
import com.example.habits.domain.usecase.GetAllHabitsUseCase
import com.example.habits.domain.usecase.ToggleHabitCompletionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel @Inject constructor(
    private val getAllHabitsUseCase: GetAllHabitsUseCase,
    private val toggleHabitCompletionUseCase: ToggleHabitCompletionUseCase,
    private val habitRecordRepository: IHabitRecordRepository,
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitListUiState(isLoading = true))
    val uiState: StateFlow<HabitListUiState> = _uiState.asStateFlow()

    init {
        analyticsService.trackEvent(
            name = "screen_viewed",
            params = mapOf("screen_name" to "habit_list")
        )
        viewModelScope.launch {
            combine(
                getAllHabitsUseCase(),
                habitRecordRepository.getRecordsForDate(LocalDate.now())
            ) { habits, todayRecords ->
                val completedIds = todayRecords.map { it.habitId }.toSet()
                val items = habits.map { habit ->
                    HabitListItem(habit = habit, isCompletedToday = habit.id in completedIds)
                }
                HabitListUiState(items = items, isLoading = false)
            }.collect { _uiState.value = it }
        }
    }

    fun toggleCompletion(habitId: Long, date: LocalDate = LocalDate.now()) {
        viewModelScope.launch {
            try {
                toggleHabitCompletionUseCase(habitId, date)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}
