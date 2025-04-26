package br.com.brunocarvalhs.friendssecrets.commons.remote.toggle

import br.com.brunocarvalhs.friendssecrets.commons.remote.RemoteProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToggleManager @Inject constructor(
    private val remoteProvider: RemoteProvider
) {

    init {
        remoteProvider.default(defaultToggle)
        remoteProvider.fetchAndActivate()
    }

    fun isFeatureEnabled(featureKey: ToggleKeys): Boolean {
        return remoteProvider.getBoolean(featureKey.toString())
    }
}
