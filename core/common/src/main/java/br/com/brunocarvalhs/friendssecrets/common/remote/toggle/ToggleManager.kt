package br.com.brunocarvalhs.friendssecrets.common.remote.toggle

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.common.remote.RemoteProvider

class ToggleManager(
    private val context: Context,
    private val remoteProvider: RemoteProvider
) {

    init {
        remoteProvider.default(defaultToggle)
        remoteProvider.fetchAndActivate()
    }

    fun isFeatureEnabled(featureKey: ToggleKeys): Boolean {
        return remoteProvider.getBoolean(featureKey.name)
    }
}
