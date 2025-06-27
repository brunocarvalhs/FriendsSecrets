package br.com.brunocarvalhs.friendssecrets.data.initialization.providers

import android.os.Bundle
import br.com.brunocarvalhs.friendssecrets.common.analytics.AnalyticsProvider
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Lazy

class AnalyticsEventImpl(
    private val firebaseAnalytics: Lazy<FirebaseAnalytics>
) : AnalyticsProvider.AnalyticsEvent {

    private val analytics: FirebaseAnalytics by lazy { firebaseAnalytics.get() }

    override fun logEvent(event: String, params: Bundle) {
        analytics.logEvent(event, params)
    }

    override fun setUserId(id: String) {
        analytics.setUserId(id)
    }
}