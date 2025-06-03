package br.com.brunocarvalhs.friendssecrets.common.analytics

import android.os.Bundle

class AnalyticsProvider(
    private val event: AnalyticsEvent
) {

    fun track(name: AnalyticsEvents, params: Map<AnalyticsParams, String> = emptyMap()) {
        event.logEvent(name.value, Bundle().apply {
            params.forEach { (key, value) -> putString(key.value, value) }
        })
    }

    interface AnalyticsEvent {
        fun logEvent(event: String, params: Bundle)
    }
}
