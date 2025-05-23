package br.com.brunocarvalhs.friendssecrets.commons.remote

import androidx.annotation.XmlRes
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.metrics.AddTrace
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class RemoteProvider(
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build()
        )
    }
) {

    @AddTrace(name = "RemoteProvider.fetchAndActivate")
    fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.i("Values updated successfully.")
                } else {
                    Timber.i("Failed to update values.")
                }
            }
    }

    @AddTrace(name = "RemoteProvider.default")
    fun default(params: Map<String, Any>) {
        remoteConfig.setDefaultsAsync(params)
    }

    @AddTrace(name = "RemoteProvider.default")
    fun default(key: String, value: Any) {
        remoteConfig.setDefaultsAsync(mapOf(key to value))
    }

    @AddTrace(name = "RemoteProvider.default")
    fun default(@XmlRes params: Int) {
        remoteConfig.setDefaultsAsync(params)
    }

    @AddTrace(name = "RemoteProvider.getLong")
    fun getLong(key: String): Long {
        return remoteConfig.getLong(key)
    }

    @AddTrace(name = "RemoteProvider.getBoolean")
    fun getBoolean(key: String): Boolean {
        return remoteConfig.getBoolean(key)
    }

    @AddTrace(name = "RemoteProvider.getString")
    fun getString(key: String): String {
        return remoteConfig.getString(key)
    }

    @AddTrace(name = "RemoteProvider.getAsyncString")
    suspend fun getAsyncString(key: String): String {
        remoteConfig.fetchAndActivate().await()
        return remoteConfig.getString(key)
    }

    @AddTrace(name = "RemoteProvider.getDouble")
    fun getDouble(key: String): Double {
        return remoteConfig.getDouble(key)
    }
}