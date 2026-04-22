package br.com.brunocarvalhs.friendssecrets

import android.app.Application
import android.os.StrictMode
import br.com.brunocarvalhs.friendssecrets.domain.services.logger.LoggerService
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
    lateinit var logger: LoggerService

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(tree = Timber.DebugTree())
        else Timber.plant(tree = logger as Timber.Tree)

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
    }
}