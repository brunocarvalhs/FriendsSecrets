package br.com.brunocarvalhs.friendssecrets.initialization.sdks

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import br.com.brunocarvalhs.friendssecrets.common.extensions.getId
import br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics.CrashlyticsProvider
import br.com.brunocarvalhs.friendssecrets.initialization.providers.CrashlyticsEventImpl
import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsInitialization : Initializer<CrashlyticsProvider> {

    override fun create(context: Context): CrashlyticsProvider {
        val crashlytics = FirebaseCrashlytics.getInstance().apply {
            isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
            setUserId(context.getId())
        }
        CrashlyticsProvider.initialize(CrashlyticsEventImpl(crashlytics))
        return CrashlyticsProvider.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(FirebaseAppInitialization::class.java)
    }
}