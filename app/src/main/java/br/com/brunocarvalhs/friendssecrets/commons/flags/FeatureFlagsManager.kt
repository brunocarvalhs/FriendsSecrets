package br.com.brunocarvalhs.friendssecrets.commons.flags

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import timber.log.Timber

class FeatureFlagsManager(
    private val remoteProvider: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
) {
    private val TAG = "FeatureFlags"

    init {
        Timber.tag(TAG).d("--> FETCH REMOTE CONFIG")
        remoteProvider.fetch().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.tag(TAG).d("<-- SUCCESS FETCH REMOTE CONFIG")
            } else {
                Timber.tag(TAG).w("<-- FAILURE FETCH REMOTE CONFIG")
            }
        }
    }

    fun isFeatureEnabled(key: String, defaultValue: Boolean = false): Boolean {
        Timber.tag(TAG).v("--> CHECK FEATURE: %s", key)
        return runCatching { 
            remoteProvider.getBoolean(key).also {
                Timber.tag(TAG).v("<-- RESULT FEATURE [%s]: %s", key, it)
            }
        }.getOrElse {
            Timber.tag(TAG).v("<-- DEFAULT FEATURE [%s]: %s", key, defaultValue)
            defaultValue
        }
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
