package br.com.brunocarvalhs.friendssecrets.commons.crashlytics

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

    fun setUserId(userId: String) {
        crashlytics.setUserId(userId)
    }

    private fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }
}

fun Throwable?.report(params: Map<String, String>? = null): Throwable? {
    if (this == null) return null
    CrashlyticsProvider.report(throwable = this, params = params)
    return this
}