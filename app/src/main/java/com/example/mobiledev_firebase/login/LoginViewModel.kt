package com.example.mobiledev_firebase.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.core.analytics.AnalyticsService
import com.example.core.auth.AuthService
import com.example.core.auth.TokenRepository
import com.example.mobiledev_firebase.firestore.UserFirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authService: AuthService,
    private val tokenRepository: TokenRepository,
    private val analyticsService: AnalyticsService,
    private val firestoreRepository: UserFirestoreRepository
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
        saveUserToFirestore(userId.toString(), email ?: "VK User", email)
        _uiState.value = LoginUiState.Success
    }

    fun onVkFailed(isCancelled: Boolean, message: String?) {
        _uiState.value = if (isCancelled) LoginUiState.Error("VK: авторизация отменена")
                         else LoginUiState.Error(message ?: "VK auth failed")
    }

    fun onYandexSuccess(accessToken: String) {
        authService.saveYandexUser(accessToken)
        analyticsService.trackEvent("user_logged_in", mapOf("provider" to "yandex"))
        val userId = getSavedUserId() ?: generateAndSaveUserId()
        saveUserToFirestore(userId, "Yandex User", null)
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

    private fun saveUserToFirestore(userId: String, name: String, email: String?) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val fcmToken = prefs.getString(KEY_FCM_TOKEN, null)
        prefs.edit().putString(KEY_USER_ID, userId).apply()
        firestoreRepository.saveUserProfile(userId, name, email, fcmToken)
    }

    private fun getSavedUserId(): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_ID, null)

    private fun generateAndSaveUserId(): String {
        val id = java.util.UUID.randomUUID().toString()
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_USER_ID, id).apply()
        return id
    }

    companion object {
        const val PREFS_NAME = "fcm_prefs"
        const val KEY_FCM_TOKEN = "fcm_token"
        const val KEY_USER_ID = "user_id"
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
