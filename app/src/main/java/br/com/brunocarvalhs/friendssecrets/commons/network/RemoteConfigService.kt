package br.com.brunocarvalhs.friendssecrets.commons.network

import br.com.brunocarvalhs.friendssecrets.domain.services.ConfigurationService
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RemoteConfigService @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : ConfigurationService {
    override fun getString(key: String, defaultValue: String): String {
        val value = remoteConfig.getString(key)
        return value.ifBlank { defaultValue }
    }
}
