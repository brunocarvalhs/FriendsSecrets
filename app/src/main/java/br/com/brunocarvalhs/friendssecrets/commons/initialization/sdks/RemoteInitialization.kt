package br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.commons.remote.RemoteProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class RemoteInitialization : Initializer<FirebaseRemoteConfig> {

    override fun create(context: Context): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(3600)
                    .build()
            )


            RemoteProvider(remoteConfig = this).fetchAndActivate()
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(FirebaseAppInitialization::class.java)
    }
}