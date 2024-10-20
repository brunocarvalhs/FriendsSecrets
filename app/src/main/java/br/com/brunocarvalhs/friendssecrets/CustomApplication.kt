package br.com.brunocarvalhs.friendssecrets

import android.app.Application
import android.content.Context
import android.provider.Settings
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.commons.logger.crashlytics.CrashlyticsProvider
import br.com.brunocarvalhs.friendssecrets.commons.logger.CrashLoggerProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        setup()
    }

    private fun setup() {
        setupFirebase()
        setupTimber()
    }

    private fun setupTimber() {
        val type = if (BuildConfig.DEBUG) Timber.DebugTree()
        else CrashLoggerProvider()

        Timber.plant(type)
    }

    private fun setupFirebase() {
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        CrashlyticsProvider.setUserId(this)
        AnalyticsProvider.setUserId(this)
    }

    companion object {
        @Volatile
        private var instance: CustomApplication? = null

        fun getInstance(): CustomApplication {
            return instance ?: synchronized(this) {
                instance ?: CustomApplication().also { instance = it }
            }
        }
    }
}