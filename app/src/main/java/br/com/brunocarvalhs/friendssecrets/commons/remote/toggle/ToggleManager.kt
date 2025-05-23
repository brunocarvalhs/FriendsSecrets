package br.com.brunocarvalhs.friendssecrets.commons.remote.toggle

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.commons.remote.RemoteProvider
import com.google.firebase.perf.metrics.AddTrace

class ToggleManager(
    private val context: Context,
    private val remoteProvider: RemoteProvider = RemoteProvider()
) {

    init {
        remoteProvider.default(defaultToggle)
        remoteProvider.fetchAndActivate()
    }

    /**
     * This method is used to check if a feature is enabled or not.
     * @param featureKey The key of the feature to be checked.
     * @return true if the feature is enabled, false otherwise.
     */
    @JvmName("isFeatureEnabled")
    @AddTrace(name = "ToggleManager.isFeatureEnabled")
    fun isFeatureEnabled(featureKey: ToggleKeys): Boolean {
        return remoteProvider.getBoolean(featureKey.toString())
    }
}
