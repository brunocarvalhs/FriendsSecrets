package br.com.brunocarvalhs.friendssecrets.initializers

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.deviceid.DeviceManager
import br.com.brunocarvalhs.deviceid.DeviceManager.Companion.DEVICE_ID
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsInitializer: Initializer<FirebaseAnalytics> {

    override fun create(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context).apply {
            setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
            DeviceManager.setCallbackDeviceId(context) {
                setUserId(it)
                setUserProperty(DEVICE_ID, it)
            }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return mutableListOf()
    }
}