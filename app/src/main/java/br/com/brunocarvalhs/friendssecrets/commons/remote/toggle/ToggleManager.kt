package br.com.brunocarvalhs.friendssecrets.commons.remote.toggle

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.commons.remote.RemoteProvider

class ToggleManager(
    private val context: Context,
    private val remoteProvider: RemoteProvider = RemoteProvider()
) {

    init {
        remoteProvider.default(defaultToggle)
        remoteProvider.fetchAndActivate()
    }

    fun isFeatureEnabled(featureKey: ToggleKeys): Boolean {
        return remoteProvider.getBoolean(featureKey.toString())
    }
}
