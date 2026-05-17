package com.example.mobiledev_firebase.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.mobiledev_firebase.MainActivity
import com.example.mobiledev_firebase.R
import com.example.mobiledev_firebase.firestore.UserFirestoreRepository
import com.example.mobiledev_firebase.login.LoginViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PushMessagingService : FirebaseMessagingService() {

    @Inject lateinit var firestoreRepository: UserFirestoreRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM Token: $token")
        getSharedPreferences(LoginViewModel.PREFS_NAME, MODE_PRIVATE)
            .edit()
            .putString(LoginViewModel.KEY_FCM_TOKEN, token)
            .apply()

        val userId = getSharedPreferences(LoginViewModel.PREFS_NAME, MODE_PRIVATE)
            .getString(LoginViewModel.KEY_USER_ID, null)
        if (userId != null) {
            firestoreRepository.updateFcmToken(userId, token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Message from: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }

        remoteMessage.notification?.let { notification ->
            showNotification(
                title = notification.title ?: "Уведомление",
                body = notification.body ?: "",
                data = remoteMessage.data
            )
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val title = data["title"] ?: return
        val body = data["body"] ?: return
        showNotification(title, body, data)
    }

    private fun showNotification(
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            data.forEach { (key, value) -> putExtra(key, value) }
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Основные уведомления",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        const val CHANNEL_ID = "push_channel_default"
        private const val TAG = "FCMService"
    }
}
