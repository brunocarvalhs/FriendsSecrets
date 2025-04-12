package br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks

import android.content.Context
import br.com.brunocarvalhs.compracerta.commons.initialization.AppInitialization
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.commons.logger.crashlytics.CrashlyticsProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsInitialization(private val context: Context) : AppInitialization() {

    override fun tag(): String  = "CrashlyticsInitialization"

    override fun execute() {
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        CrashlyticsProvider.setUserId(context)
        AnalyticsProvider.setUserId(context)
    }
}