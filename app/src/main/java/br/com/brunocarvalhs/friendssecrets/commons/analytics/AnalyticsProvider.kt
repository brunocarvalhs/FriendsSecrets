package br.com.brunocarvalhs.friendssecrets.commons.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object AnalyticsProvider {
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun track(event: AnalyticsEvents, params: Map<AnalyticsParams, String> = emptyMap()) {
        firebaseAnalytics.logEvent(event.value, Bundle().apply {
            params.forEach { (key, value) -> putString(key.value, value) }
        })
    }
}
