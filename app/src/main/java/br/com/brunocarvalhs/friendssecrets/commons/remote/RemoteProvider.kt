package br.com.brunocarvalhs.friendssecrets.commons.remote

import androidx.annotation.XmlRes
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class RemoteProvider(
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig,
) {

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

    fun default(params: Map<String, Any>) {
        remoteConfig.setDefaultsAsync(params)
    }

    fun default(key: String, value: Any) {
        remoteConfig.setDefaultsAsync(mapOf(key to value))
    }

    fun default(@XmlRes params: Int) {
        remoteConfig.setDefaultsAsync(params)
    }

    fun getLong(key: String): Long {
        return remoteConfig.getLong(key)
    }

    fun getBoolean(key: String): Boolean {
        return remoteConfig.getBoolean(key)
    }

    fun getString(key: String): String {
        return remoteConfig.getString(key)
    }

    suspend fun getAsyncString(key: String): String {
        remoteConfig.fetchAndActivate().await()
        return remoteConfig.getString(key)
    }

    fun getDouble(key: String): Double {
        return remoteConfig.getDouble(key)
    }
}