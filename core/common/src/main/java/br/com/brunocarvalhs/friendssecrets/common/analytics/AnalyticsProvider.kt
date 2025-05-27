package br.com.brunocarvalhs.friendssecrets.common.analytics

import android.os.Bundle
import timber.log.Timber

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

    companion object {
        private var instance: AnalyticsProvider? = null

        fun initialize(event: AnalyticsEvent) {
            synchronized(this) {
                if (instance == null) {
                    instance = AnalyticsProvider(event)
                } else {
                    Timber.tag("AnalyticsProvider").w("Provider já inicializado.")
                }
            }
        }

        fun getInstance(): AnalyticsProvider {
            return instance ?: synchronized(this) {
                instance
                    ?: throw IllegalStateException("AnalyticsProvider não inicializado. Chame initialize() primeiro.")
            }
        }
    }
}
