package com.example.mobileedv_firebase.analytics

import com.example.core.analytics.AnalyticsService
import io.appmetrica.analytics.AppMetrica

class AppMetricaAnalyticsService : AnalyticsService {

    override fun trackEvent(name: String, params: Map<String, Any>) {
        if (params.isEmpty()) {
            AppMetrica.reportEvent(name)
        } else {
            AppMetrica.reportEvent(name, params.mapValues { it.value.toString() })
        }
    }

    override fun trackError(message: String, error: Throwable?) {
        AppMetrica.reportError(message, error)
    }
}
