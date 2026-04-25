package br.com.brunocarvalhs.friendssecrets.initializers

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.deviceid.DeviceManager
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsInitializer: Initializer<FirebaseCrashlytics> {

    override fun create(context: Context): FirebaseCrashlytics {
        return FirebaseCrashlytics.getInstance().apply {
            isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
            DeviceManager.setCallbackDeviceId(context) { setUserId(it) }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return mutableListOf(FirebaseInitializer::class.java)
    }
}
