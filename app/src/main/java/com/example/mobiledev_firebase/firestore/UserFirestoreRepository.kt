package com.example.mobiledev_firebase.firestore

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFirestoreRepository @Inject constructor() {

    private val db = Firebase.firestore

    fun saveUserProfile(userId: String, name: String, email: String?, fcmToken: String?) {
        val data = hashMapOf(
            "name" to name,
            "email" to email,
            "fcmToken" to fcmToken,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        db.collection("users").document(userId)
            .set(data)
            .addOnSuccessListener { Log.d(TAG, "Profile saved: $userId") }
            .addOnFailureListener { Log.w(TAG, "Failed to save profile", it) }
    }

    fun updateFcmToken(userId: String, fcmToken: String) {
        db.collection("users").document(userId)
            .update(
                "fcmToken", fcmToken,
                "updatedAt", FieldValue.serverTimestamp()
            )
            .addOnSuccessListener { Log.d(TAG, "FCM token updated: $userId") }
            .addOnFailureListener { Log.w(TAG, "Failed to update FCM token", it) }
    }

    fun observeUserProfile(userId: String, onUpdate: (Map<String, Any?>) -> Unit): ListenerRegistration {
        return db.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Snapshot listener failed", error)
                    return@addSnapshotListener
                }
                snapshot?.data?.let { onUpdate(it) }
            }
    }

    companion object {
        private const val TAG = "UserFirestore"
    }
}
