package br.com.brunocarvalhs.friendssecrets.initialization.sdks

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import br.com.brunocarvalhs.friendssecrets.common.logger.CrashLoggerProvider
import timber.log.Timber

class TimberInitialization : Initializer<Timber.Forest> {

    override fun create(context: Context): Timber.Forest {
        val type: Timber.Tree = if (BuildConfig.DEBUG) Timber.DebugTree()
        else CrashLoggerProvider()
        return Timber.apply { plant(type) }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(CrashlyticsInitialization::class.java)
    }
}