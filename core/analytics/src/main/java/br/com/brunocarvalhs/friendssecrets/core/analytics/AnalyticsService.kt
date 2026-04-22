package br.com.brunocarvalhs.friendssecrets.core.analytics

interface AnalyticsService {
    fun logEvent(name: String, params: Map<String, Any?> = emptyMap())
    fun setUserProperty(name: String, value: String)
    fun setUserId(userId: String)
}
