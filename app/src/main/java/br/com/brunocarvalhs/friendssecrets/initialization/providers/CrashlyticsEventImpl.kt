package br.com.brunocarvalhs.friendssecrets.initialization.providers

import br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics.CrashlyticsProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsEventImpl(
    private val firebaseCrashlytics: FirebaseCrashlytics
) : CrashlyticsProvider.CrashlyticsEvent {

    override fun report(throwable: Throwable) {
        firebaseCrashlytics.recordException(throwable)
    }

    override fun log(message: String) {
        firebaseCrashlytics.log(message)
    }

    override fun parameter(key: String, value: String) {
        firebaseCrashlytics.setCustomKey(key, value)
    }
}