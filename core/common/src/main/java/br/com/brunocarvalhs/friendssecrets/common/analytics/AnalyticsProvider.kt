package br.com.brunocarvalhs.friendssecrets.common.analytics

import android.os.Bundle

class AnalyticsProvider(
    private val event: AnalyticsEvent
) {
    init {
        instance = this
    }

    fun track(name: AnalyticsEvents, params: Map<AnalyticsParams, String> = emptyMap()) {
        event.logEvent(name.value, Bundle().apply {
            params.forEach { (key, value) -> putString(key.value, value) }
        })
    }

    interface AnalyticsEvent {
        fun logEvent(event: String, params: Bundle)
        fun setUserId(id: String)
    }

    companion object {
        @Volatile
        private var instance: AnalyticsProvider? = null

        @JvmStatic
        fun getInstance(): AnalyticsProvider {
            return instance ?: throw IllegalStateException("AnalyticsProvider not initialized")
        }

        @JvmStatic
        fun initialize(event: AnalyticsEvent) {
            instance = AnalyticsProvider(event)
        }
    }
}
