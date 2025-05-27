package br.com.brunocarvalhs.friendssecrets.initialization.providers

import android.os.Bundle
import br.com.brunocarvalhs.friendssecrets.common.analytics.AnalyticsProvider
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsEventImpl(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsProvider.AnalyticsEvent {
    override fun logEvent(event: String, params: Bundle) {
        firebaseAnalytics.logEvent(event, params)
    }
}