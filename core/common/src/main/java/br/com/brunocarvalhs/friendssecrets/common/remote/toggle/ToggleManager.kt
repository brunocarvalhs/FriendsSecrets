package br.com.brunocarvalhs.friendssecrets.common.remote.toggle

import br.com.brunocarvalhs.friendssecrets.common.remote.RemoteProvider

class ToggleManager(
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
