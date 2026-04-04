package br.com.brunocarvalhs.friendssecrets.commons.flags

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class FeatureFlagsManager(
    private val remoteProvider: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
) {

    init {
        remoteProvider.fetch()
    }

    fun isFeatureEnabled(key: String, defaultValue: Boolean = false): Boolean {
        return runCatching { remoteProvider.getBoolean(key) }.getOrDefault(defaultValue)
    }

    companion object {
        @Volatile
        private var instance: FeatureFlagsManager? = null

        @JvmStatic
        fun getInstance(): FeatureFlagsManager {
            return instance ?: synchronized(this) {
                instance ?: FeatureFlagsManager().also { instance = it }
            }
        }
    }
}