package br.com.brunocarvalhs.friendssecrets.initialization.providers

import br.com.brunocarvalhs.friendssecrets.common.remote.RemoteProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

@Suppress("UNCHECKED_CAST")
class RemoteEventImpl(
    private val remoteConfig: FirebaseRemoteConfig
) : RemoteProvider.RemoteEvent {

    override fun <T> getValue(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> remoteConfig.getString(key) as T
            is Boolean -> remoteConfig.getBoolean(key) as T
            is Long -> remoteConfig.getLong(key) as T
            is Double -> remoteConfig.getDouble(key) as T
            else -> defaultValue
        }
    }

    override suspend fun <T> getValueAsync(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> remoteConfig.getString(key) as T
            is Boolean -> remoteConfig.getBoolean(key) as T
            is Long -> remoteConfig.getLong(key) as T
            is Double -> remoteConfig.getDouble(key) as T
            else -> defaultValue
        }
    }

    override fun defaultValues(defaultValues: Map<String, Any>) {
        remoteConfig.setDefaultsAsync(defaultValues)
    }

    override fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
    }
}