package br.com.brunocarvalhs.core.analytics

interface AnalyticsService {
    fun logEvent(name: String, params: Map<String, Any?> = emptyMap())
    fun setUserProperty(name: String, value: String)
    fun setUserId(userId: String)
}
