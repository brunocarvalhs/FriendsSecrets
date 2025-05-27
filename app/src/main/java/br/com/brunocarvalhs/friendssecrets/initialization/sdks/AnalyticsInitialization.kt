package br.com.brunocarvalhs.friendssecrets.initialization.sdks

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.common.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.common.extensions.getId
import br.com.brunocarvalhs.friendssecrets.initialization.providers.AnalyticsEventImpl
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsInitialization : Initializer<AnalyticsProvider> {

    override fun create(context: Context): AnalyticsProvider {
        val analytics = FirebaseAnalytics.getInstance(context).apply {
            setUserId(context.getId())
        }
        AnalyticsProvider.initialize(AnalyticsEventImpl(analytics))
        return AnalyticsProvider.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(FirebaseAppInitialization::class.java)
    }
}