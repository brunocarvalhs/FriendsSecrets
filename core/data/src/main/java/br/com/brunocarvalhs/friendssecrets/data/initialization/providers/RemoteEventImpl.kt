package br.com.brunocarvalhs.friendssecrets.data.initialization.providers

import br.com.brunocarvalhs.friendssecrets.common.remote.RemoteProvider
import com.google.firebase.perf.metrics.AddTrace
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Lazy

@Suppress("UNCHECKED_CAST")
class RemoteEventImpl(
    private val remoteConfig: Lazy<FirebaseRemoteConfig>
) : RemoteProvider.RemoteEvent {

    private val remote: FirebaseRemoteConfig by lazy { remoteConfig.get() }

    @AddTrace(name = "RemoteEventImpl.getValue", enabled = true)
    override fun <T> getValue(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> remote.getString(key) as T
            is Boolean -> remote.getBoolean(key) as T
            is Long -> remote.getLong(key) as T
            is Double -> remote.getDouble(key) as T
            else -> defaultValue
        }
    }

    @AddTrace(name = "RemoteEventImpl.getValueAsync", enabled = true)
    override suspend fun <T> getValueAsync(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> remote.getString(key) as T
            is Boolean -> remote.getBoolean(key) as T
            is Long -> remote.getLong(key) as T
            is Double -> remote.getDouble(key) as T
            else -> defaultValue
        }
    }

    @AddTrace(name = "RemoteEventImpl.defaultValues", enabled = true)
    override fun defaultValues(defaultValues: Map<String, Any>) {
        remote.setDefaultsAsync(defaultValues)
    }

    @AddTrace(name = "RemoteEventImpl.fetchAndActivate", enabled = true)
    override fun fetchAndActivate() {
        remote.fetchAndActivate()
    }
}