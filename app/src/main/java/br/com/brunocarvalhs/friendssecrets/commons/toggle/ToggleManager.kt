package br.com.brunocarvalhs.friendssecrets.commons.toggle

import android.content.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import timber.log.Timber

class ToggleManager(
    private val context: Context
) {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(360)
                .build()
        )

        setDefaultsAsync(defaultToggle)
    }

    init {
        fetchAndActivate()
    }

    private fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.i("Values updated successfully.")
                } else {
                    Timber.i("Failed to update values.")
                }
            }
    }

    fun isFeatureEnabled(featureKey: String): Boolean {
        return remoteConfig.getBoolean(featureKey)
    }
}
