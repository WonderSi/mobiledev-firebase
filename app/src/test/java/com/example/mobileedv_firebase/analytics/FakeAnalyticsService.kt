package com.example.mobileedv_firebase.analytics

import android.util.Log
import com.example.core.analytics.AnalyticsService

class FakeAnalyticsService : AnalyticsService {

    // Список всех вызовов trackEvent — тесты проверяют этот список
    val trackedEvents = mutableListOf<Pair<String, Map<String, Any>>>()
    val trackedErrors = mutableListOf<Pair<String, Throwable?>>()

    override fun trackEvent(name: String, params: Map<String, Any>) {
        trackedEvents.add(name to params)
    }

    override fun trackError(message: String, error: Throwable?) {
        trackedErrors.add(message to error)
    }

    fun clear() {
        trackedEvents.clear()
        trackedErrors.clear()
    }
}
