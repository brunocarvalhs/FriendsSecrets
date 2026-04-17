package br.com.brunocarvalhs.friendssecrets.commons.flags

import br.com.brunocarvalhs.friendssecrets.domain.services.FeatureFlagService
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FeatureFlagsManager @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : FeatureFlagService {

    init {
        Timber.tag(TAG).d("--> FETCH REMOTE CONFIG")
        remoteConfig.fetch().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.tag(TAG).d("<-- SUCCESS FETCH REMOTE CONFIG")
            } else {
                Timber.tag(TAG).w("<-- FAILURE FETCH REMOTE CONFIG")
            }
        }
    }

    override fun validate(key: String): Boolean {
        Timber.tag(TAG).v("--> CHECK FEATURE: %s", key)
        return remoteConfig.getBoolean(key).also {
            Timber.tag(TAG).d("<-- SUCCESS CHECK FEATURE: %s", key)
        }
    }

    override fun validate(key: String, defaultValue: Boolean): Boolean {
        Timber.tag(TAG).v("--> CHECK FEATURE: %s", key)
        val value = remoteConfig.getBoolean(key)
        return if (remoteConfig.all.containsKey(key)) {
            Timber.tag(TAG).d("<-- SUCCESS CHECK FEATURE: %s", key)
            value
        } else {
            Timber.tag(TAG).d("<-- FAILURE CHECK FEATURE: %s", key)
            defaultValue
        }
    }

    companion object {
        private const val TAG = "FeatureFlags"
    }
}