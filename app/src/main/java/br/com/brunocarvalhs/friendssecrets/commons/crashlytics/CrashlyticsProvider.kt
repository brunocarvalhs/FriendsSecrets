package br.com.brunocarvalhs.friendssecrets.commons.crashlytics

import android.content.Context
import android.provider.Settings.Secure
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

object CrashlyticsProvider {

    private val crashlytics: FirebaseCrashlytics = Firebase.crashlytics

    fun report(throwable: Throwable, params: Map<String, String>? = null) {
        params?.forEach { (key, value) -> setCustomKey(key, value) }
        crashlytics.recordException(throwable)
    }

    fun log(message: String, params: Map<String, String>? = null) {
        params?.forEach { (key, value) -> setCustomKey(key, value) }
        crashlytics.log(message)
    }

    fun setUserId(context: Context) {
        val deviceId =
            Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        crashlytics.setUserId(deviceId)
    }

    private fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }
}
