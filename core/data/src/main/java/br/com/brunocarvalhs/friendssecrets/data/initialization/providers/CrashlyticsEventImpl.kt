package br.com.brunocarvalhs.friendssecrets.data.initialization.providers

import br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics.CrashlyticsProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.metrics.AddTrace
import dagger.Lazy

class CrashlyticsEventImpl(
    private val firebaseCrashlytics: Lazy<FirebaseCrashlytics>
) : CrashlyticsProvider.CrashlyticsEvent {

    private val crashlytics: FirebaseCrashlytics by lazy { firebaseCrashlytics.get() }

    @AddTrace(name = "CrashlyticsEventImpl.report", enabled = true)
    override fun report(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    @AddTrace(name = "CrashlyticsEventImpl.log", enabled = true)
    override fun log(message: String) {
        crashlytics.log(message)
    }

    @AddTrace(name = "CrashlyticsEventImpl.parameter", enabled = true)
    override fun parameter(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    @AddTrace(name = "CrashlyticsEventImpl.setUserId", enabled = true)
    override fun setUserId(id: String) {
        crashlytics.setUserId(id)
    }
}