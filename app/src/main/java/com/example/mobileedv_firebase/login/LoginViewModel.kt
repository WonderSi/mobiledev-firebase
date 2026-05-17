package com.example.mobileedv_firebase.login

import androidx.lifecycle.ViewModel
import com.example.core.analytics.AnalyticsService
import com.example.core.auth.AuthService
import com.example.core.auth.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val tokenRepository: TokenRepository,
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun isAlreadyLoggedIn(): Boolean = tokenRepository.isLoggedIn()

    fun startLoading() {
        _uiState.value = LoginUiState.Loading
    }

    fun onVkSuccess(accessToken: String, userId: Long, email: String?) {
        authService.saveVkUser(accessToken, userId, email)
        analyticsService.trackEvent("user_logged_in", mapOf("provider" to "vk"))
        _uiState.value = LoginUiState.Success
    }

    fun onVkFailed(isCancelled: Boolean, message: String?) {
        _uiState.value = if (isCancelled) LoginUiState.Error("VK: авторизация отменена")
                         else LoginUiState.Error(message ?: "VK auth failed")
    }

    fun onYandexSuccess(accessToken: String) {
        authService.saveYandexUser(accessToken)
        analyticsService.trackEvent("user_logged_in", mapOf("provider" to "yandex"))
        _uiState.value = LoginUiState.Success
    }

    fun onAuthError(message: String) {
        _uiState.value = LoginUiState.Error(message)
    }

    fun onAuthCancelled() {
        _uiState.value = LoginUiState.Idle
    }

    fun logout() {
        authService.logout()
        _uiState.value = LoginUiState.Idle
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
