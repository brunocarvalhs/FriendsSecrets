package br.com.brunocarvalhs.friendssecrets.commons.logger

import android.util.Log
import br.com.brunocarvalhs.friendssecrets.commons.logger.crashlytics.CrashlyticsProvider
import com.google.firebase.perf.metrics.AddTrace
import timber.log.Timber

class CrashLoggerProvider(
    private val crashlytics: CrashlyticsProvider = CrashlyticsProvider
) : Timber.Tree() {

    @AddTrace(name = "CrashLoggerProvider.log")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when(priority) {
            Log.VERBOSE, Log.DEBUG, Log.INFO, Log.ASSERT -> return
            Log.ERROR -> crashlytics.report(throwable = t ?: Exception(message))
            Log.WARN -> crashlytics.log(message = message)
        }
    }
}