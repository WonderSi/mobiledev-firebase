package com.example.mobiledev_firebase.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.mobiledev_firebase.firestore.UserFirestoreRepository
import com.example.mobiledev_firebase.login.LoginViewModel
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestoreRepository: UserFirestoreRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<Map<String, Any?>>(emptyMap())
    val profile: StateFlow<Map<String, Any?>> = _profile.asStateFlow()

    private var listenerRegistration: ListenerRegistration? = null

    init {
        val userId = context.getSharedPreferences(LoginViewModel.PREFS_NAME, Context.MODE_PRIVATE)
            .getString(LoginViewModel.KEY_USER_ID, null)
        if (userId != null) {
            listenerRegistration = firestoreRepository.observeUserProfile(userId) { data ->
                _profile.value = data
            }
        }
    }

    override fun onCleared() {
        listenerRegistration?.remove()
        super.onCleared()
    }
}
