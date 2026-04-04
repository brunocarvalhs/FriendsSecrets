package br.com.brunocarvalhs.friendssecrets.commons.sdks

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import br.com.brunocarvalhs.friendssecrets.commons.security.DeviceManager
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsInitializer : Initializer<FirebaseAnalytics> {

    override fun create(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context).apply {
            setAnalyticsCollectionEnabled(true)
            setUserId(DeviceManager.getDeviceIdentifier(context))
            setUserProperty("app_version", BuildConfig.VERSION_NAME)
            setUserProperty("app_build_type", BuildConfig.BUILD_TYPE)
            setUserProperty("app_package_name", BuildConfig.APPLICATION_ID)
            setUserProperty("app_build_number", BuildConfig.VERSION_CODE.toString())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }
}