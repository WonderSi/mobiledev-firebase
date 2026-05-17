package com.example.mobiledev_firebase.messaging

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM Token: $token")
        getSharedPreferences(FCM_PREFS, MODE_PRIVATE)
            .edit()
            .putString(KEY_FCM_TOKEN, token)
            .apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Message from: ${remoteMessage.from}")
        Log.d(TAG, "Data: ${remoteMessage.data}")
        remoteMessage.notification?.let {
            Log.d(TAG, "Notification title: ${it.title}, body: ${it.body}")
        }
    }

    companion object {
        const val FCM_PREFS = "fcm_prefs"
        const val KEY_FCM_TOKEN = "fcm_token"
        private const val TAG = "FCMService"
    }
}
