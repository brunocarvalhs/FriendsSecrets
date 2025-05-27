package br.com.brunocarvalhs.friendssecrets.initialization.sdks

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.common.remote.RemoteProvider
import br.com.brunocarvalhs.friendssecrets.initialization.providers.RemoteEventImpl
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class RemoteInitialization : Initializer<RemoteProvider> {

    override fun create(context: Context): RemoteProvider {
        val remoteConfig = FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(3600)
                    .build()
            )
        }
        RemoteProvider.initialize(RemoteEventImpl(remoteConfig))
        return RemoteProvider.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(FirebaseAppInitialization::class.java)
    }
}