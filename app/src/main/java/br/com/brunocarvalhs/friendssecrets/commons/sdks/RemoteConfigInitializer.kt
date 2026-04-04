package br.com.brunocarvalhs.friendssecrets.commons.sdks

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class RemoteConfigInitializer: Initializer<FirebaseRemoteConfig> {
    override fun create(context: Context): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance()
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }
}