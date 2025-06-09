package br.com.brunocarvalhs.friendssecrets.data.initialization.providers

import br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics.CrashlyticsProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Lazy

class CrashlyticsEventImpl(
    private val firebaseCrashlytics: Lazy<FirebaseCrashlytics>
) : CrashlyticsProvider.CrashlyticsEvent {

    private val crashlytics: FirebaseCrashlytics by lazy { firebaseCrashlytics.get() }

    override fun report(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun log(message: String) {
        crashlytics.log(message)
    }

    override fun parameter(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    override fun setUserId(id: String) {
        crashlytics.setUserId(id)
    }
}