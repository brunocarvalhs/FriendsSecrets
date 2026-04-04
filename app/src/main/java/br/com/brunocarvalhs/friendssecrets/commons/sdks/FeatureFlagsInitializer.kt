package br.com.brunocarvalhs.friendssecrets.commons.sdks

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.commons.flags.FeatureFlagsManager

class FeatureFlagsInitializer: Initializer<FeatureFlagsManager> {

    override fun create(context: Context): FeatureFlagsManager {
        return FeatureFlagsManager.getInstance()
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return mutableListOf(RemoteConfigInitializer::class.java)
    }
}