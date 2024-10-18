package br.com.brunocarvalhs.friendssecrets.commons.toggle

import android.content.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig

class ToggleManager(
    private val context: Context
) {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build()
        )
    }

    init {
        fetchAndActivate()
    }

    private fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Values updated successfully.")
                } else {
                    println("Failed to update values.")
                }
            }
    }

    fun isFeatureEnabled(featureKey: String): Boolean {
        return remoteConfig.getBoolean(featureKey)
    }
}
