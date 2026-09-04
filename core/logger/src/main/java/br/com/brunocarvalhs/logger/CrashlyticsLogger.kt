package br.com.brunocarvalhs.logger

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import java.util.concurrent.CancellationException
import javax.inject.Inject

class CrashlyticsLogger @Inject constructor(
    private val crashlytics: FirebaseCrashlytics
) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (t is CancellationException) return

        when (priority) {
            Log.VERBOSE, Log.DEBUG, Log.INFO, Log.ASSERT -> return
            Log.ERROR -> crashlytics.recordException(t ?: Exception(message))
            Log.WARN -> crashlytics.log(message)
        }
    }
}
