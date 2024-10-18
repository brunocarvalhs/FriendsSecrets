package br.com.brunocarvalhs.friendssecrets

import android.app.Application
import br.com.brunocarvalhs.friendssecrets.commons.logger.CrashLoggerProvider
import timber.log.Timber

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        setup()
    }

    private fun setup() {
        setupTimber()
    }

    private fun setupTimber() {
        val type = if (BuildConfig.DEBUG) Timber.DebugTree()
        else CrashLoggerProvider()

        Timber.plant(type)
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