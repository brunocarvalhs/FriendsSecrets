package br.com.brunocarvalhs.friendssecrets.commons.logger.crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

object CrashlyticsProvider {

    private val crashlytics: FirebaseCrashlytics by lazy { Firebase.crashlytics }

    fun report(throwable: Throwable, params: Map<String, String>? = null) {
        params?.forEach { (key, value) -> setCustomKey(key, value) }
        crashlytics.recordException(throwable)
    }

    fun log(message: String, params: Map<String, String>? = null) {
        params?.forEach { (key, value) -> setCustomKey(key, value) }
        crashlytics.log(message)
    }

    private fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }
}
