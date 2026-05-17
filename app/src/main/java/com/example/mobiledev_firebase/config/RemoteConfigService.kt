package com.example.mobiledev_firebase.config

import android.util.Log
import com.example.mobiledev_firebase.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class RemoteConfigService {

    private val remoteConfig = Firebase.remoteConfig

    init {
        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // 0 для тестирования, в продакшне >= 3600
        }
        remoteConfig.setConfigSettingsAsync(settings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    fun fetchAndApply(onComplete: (Boolean) -> Unit = {}) {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Remote config fetched and activated")
            } else {
                Log.w(TAG, "Remote config fetch failed", task.exception)
            }
            onComplete(task.isSuccessful)
        }
    }

    fun getWelcomeMessage(): String = remoteConfig.getString("welcome_message")

    fun isNewFeatureEnabled(): Boolean = remoteConfig.getBoolean("is_new_feature_enabled")

    companion object {
        private const val TAG = "RemoteConfig"
    }
}
