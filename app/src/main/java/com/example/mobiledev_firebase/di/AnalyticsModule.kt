package com.example.mobiledev_firebase.di

import com.example.core.analytics.AnalyticsService
import com.example.mobiledev_firebase.analytics.AppMetricaAnalyticsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideAnalyticsService(): AnalyticsService = AppMetricaAnalyticsService()
}
