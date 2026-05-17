package com.example.habits.ui.habitlist

import com.example.habits.domain.model.Habit
import com.example.habits.domain.model.HabitRecord
import com.example.habits.domain.repository.IHabitRecordRepository
import com.example.habits.domain.repository.IHabitRepository
import com.example.habits.domain.usecase.GetAllHabitsUseCase
import com.example.habits.domain.usecase.ToggleHabitCompletionUseCase
import com.example.habits.ui.FakeAnalyticsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class HabitListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var analytics: FakeAnalyticsService
    private lateinit var viewModel: HabitListViewModel

    // Фейковые репозитории — возвращают пустые данные, нас интересует только аналитика
    private val fakeHabitRepository = object : IHabitRepository {
        override fun getAll(): Flow<List<Habit>> = flowOf(emptyList())
        override suspend fun getById(id: Long): Habit? = null
        override suspend fun insert(habit: Habit): Long = 0L
        override suspend fun update(habit: Habit) = Unit
        override suspend fun delete(id: Long) = Unit
    }

    private val fakeRecordRepository = object : IHabitRecordRepository {
        override fun getRecordsForHabit(habitId: Long): Flow<List<HabitRecord>> = flowOf(emptyList())
        override fun getRecordsForDate(date: LocalDate): Flow<List<HabitRecord>> = flowOf(emptyList())
        override suspend fun getRecordForDate(habitId: Long, date: LocalDate): HabitRecord? = null
        override suspend fun insert(record: HabitRecord) = Unit
        override suspend fun delete(id: Long) = Unit
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        analytics = FakeAnalyticsService()
        viewModel = HabitListViewModel(
            getAllHabitsUseCase = GetAllHabitsUseCase(fakeHabitRepository),
            toggleHabitCompletionUseCase = ToggleHabitCompletionUseCase(fakeHabitRepository, fakeRecordRepository),
            habitRecordRepository = fakeRecordRepository,
            analyticsService = analytics
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Тест 1: при создании ViewModel отправляется событие screen_viewed
    @Test
    fun `when ViewModel is created, screen_viewed event is tracked`() = runTest {
        assertTrue(
            "Ожидалось событие screen_viewed",
            analytics.trackedEvents.any { it.first == "screen_viewed" }
        )
    }

    // Тест 2: событие screen_viewed содержит параметр screen_name = habit_list
    @Test
    fun `screen_viewed event contains correct screen_name parameter`() = runTest {
        val event = analytics.trackedEvents.first { it.first == "screen_viewed" }
        assertEquals(
            "Ожидался screen_name = habit_list",
            "habit_list",
            event.second["screen_name"]
        )
    }
}
