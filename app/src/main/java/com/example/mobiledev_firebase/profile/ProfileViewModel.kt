package com.example.mobiledev_firebase.profile

import androidx.lifecycle.ViewModel
import com.example.mobiledev_firebase.firestore.UserFirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestoreRepository: UserFirestoreRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<Map<String, Any?>>(emptyMap())
    val profile: StateFlow<Map<String, Any?>> = _profile.asStateFlow()

    private var listenerRegistration: ListenerRegistration? = null

    init {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            listenerRegistration = firestoreRepository.observeUserProfile(uid) { data ->
                _profile.value = data
            }
        }
    }

    override fun onCleared() {
        listenerRegistration?.remove()
        super.onCleared()
    }
}
