package com.example.mobileedv_firebase.di

import android.content.Context
import com.example.core.auth.AuthService
import com.example.core.auth.TokenRepository
import com.example.mobileedv_firebase.auth.AuthServiceImpl
import com.example.mobileedv_firebase.auth.TokenRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideTokenRepository(@ApplicationContext context: Context): TokenRepository =
        TokenRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideAuthService(tokenRepository: TokenRepository): AuthService =
        AuthServiceImpl(tokenRepository)
}
