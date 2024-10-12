package br.com.brunocarvalhs.friendssecrets

import android.app.Application
import br.com.brunocarvalhs.friendssecrets.commons.logger.CrashLoggerProvider
import timber.log.Timber

class CustomApplication : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
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
        lateinit var instance: CustomApplication
    }
}