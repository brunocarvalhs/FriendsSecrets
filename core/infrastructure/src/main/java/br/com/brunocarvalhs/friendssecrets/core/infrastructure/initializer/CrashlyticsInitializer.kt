package br.com.brunocarvalhs.friendssecrets.core.infrastructure.initializer

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.device.DeviceManager
import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsInitializer : Initializer<FirebaseCrashlytics> {

    override fun create(context: Context): FirebaseCrashlytics {
        return FirebaseCrashlytics.getInstance().apply {
            isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
            setCustomKey("app_version", BuildConfig.VERSION_NAME)
            setCustomKey("app_build_type", BuildConfig.BUILD_TYPE)
            setCustomKey("app_package_name", BuildConfig.APPLICATION_ID)
            setCustomKey("app_build_number", BuildConfig.VERSION_CODE.toString())
            DeviceManager.setCallbackDeviceId(context) { deviceId -> setUserId(deviceId) }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }
}