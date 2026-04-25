package br.com.brunocarvalhs.friendssecrets.initializers

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import br.com.brunocarvalhs.logger.CrashlyticsLogger
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import timber.log.Timber

class LoggerInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(tree = Timber.DebugTree())
        } else {
            Timber.plant(tree = CrashlyticsLogger(Firebase.crashlytics))
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return mutableListOf(CrashlyticsInitializer::class.java)
    }
}