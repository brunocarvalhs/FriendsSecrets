package br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks

import br.com.brunocarvalhs.compracerta.commons.initialization.AppInitialization
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import br.com.brunocarvalhs.friendssecrets.commons.logger.CrashLoggerProvider
import timber.log.Timber

class TimberInitialization : AppInitialization() {

    override fun execute() {
        val type = if (BuildConfig.DEBUG) Timber.DebugTree()
        else CrashLoggerProvider()

        Timber.plant(type)
    }

    override fun tag(): String = "TimberInitialization"
}