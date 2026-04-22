package br.com.brunocarvalhs.friendssecrets.core.infrastructure.logger

import android.util.Log
import br.com.brunocarvalhs.friendssecrets.domain.services.logger.LoggerService
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import javax.inject.Inject

class CrashlyticsLogger @Inject constructor(
    private val crashlytics: FirebaseCrashlytics
) : Timber.Tree(), LoggerService {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.VERBOSE, Log.DEBUG, Log.INFO, Log.ASSERT -> return
            Log.ERROR -> crashlytics.recordException(t ?: Exception(message))
            Log.WARN -> crashlytics.log(message)
        }
    }
}
