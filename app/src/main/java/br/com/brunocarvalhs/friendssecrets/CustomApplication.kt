package br.com.brunocarvalhs.friendssecrets

import android.app.Application
import br.com.brunocarvalhs.logger.CrashlyticsLogger
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * Application class with Hilt dependency injection setup.
 *
 * The @HiltAndroidApp annotation triggers Hilt's code generation and
 * creates the application-level component that provides dependencies
 * to all screens and components in the application.
 *
 * This class initializes the application and sets up strict mode for debugging
 * to catch potential performance issues during development.
 */
@HiltAndroidApp
class CustomApplication : Application() {
    @Inject
    lateinit var crashlyticsLogger: CrashlyticsLogger

    override fun onCreate() {
        super.onCreate()
        setupLogger()
    }

    private fun setupLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(crashlyticsLogger)
        }
        Timber.d("Timber initialized 🚀")
    }
}