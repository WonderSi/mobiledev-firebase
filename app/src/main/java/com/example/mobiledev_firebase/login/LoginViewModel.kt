package com.example.mobiledev_firebase.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.core.analytics.AnalyticsService
import com.example.core.auth.AuthService
import com.example.core.auth.TokenRepository
import com.example.mobiledev_firebase.firestore.UserFirestoreRepository
import com.google.firebase.auth.FirebaseAuth
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
        signInAnonymouslyAndSave(email ?: "VK User", email)
    }

    fun onVkFailed(isCancelled: Boolean, message: String?) {
        _uiState.value = if (isCancelled) LoginUiState.Error("VK: авторизация отменена")
                         else LoginUiState.Error(message ?: "VK auth failed")
    }

    fun onYandexSuccess(accessToken: String) {
        authService.saveYandexUser(accessToken)
        analyticsService.trackEvent("user_logged_in", mapOf("provider" to "yandex"))
        signInAnonymouslyAndSave("Yandex User", null)
    }

    fun onAuthError(message: String) {
        _uiState.value = LoginUiState.Error(message)
    }

    fun onAuthCancelled() {
        _uiState.value = LoginUiState.Idle
    }

    fun logout() {
        authService.logout()
        FirebaseAuth.getInstance().signOut()
        _uiState.value = LoginUiState.Idle
    }

    private fun signInAnonymouslyAndSave(name: String, email: String?) {
        val auth = FirebaseAuth.getInstance()
        val current = auth.currentUser
        if (current != null) {
            saveUserToFirestore(current.uid, name, email)
            _uiState.value = LoginUiState.Success
            return
        }
        auth.signInAnonymously()
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                Log.d(TAG, "Signed in anonymously: $uid")
                saveUserToFirestore(uid, name, email)
                _uiState.value = LoginUiState.Success
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Anonymous sign-in failed", e)
                _uiState.value = LoginUiState.Error("Auth error: ${e.message}")
            }
    }

    private fun saveUserToFirestore(userId: String, name: String, email: String?) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val fcmToken = prefs.getString(KEY_FCM_TOKEN, null)
        prefs.edit().putString(KEY_USER_ID, userId).apply()
        firestoreRepository.saveUserProfile(userId, name, email, fcmToken)
    }

    companion object {
        const val PREFS_NAME = "fcm_prefs"
        const val KEY_FCM_TOKEN = "fcm_token"
        const val KEY_USER_ID = "user_id"
        private const val TAG = "LoginViewModel"
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
