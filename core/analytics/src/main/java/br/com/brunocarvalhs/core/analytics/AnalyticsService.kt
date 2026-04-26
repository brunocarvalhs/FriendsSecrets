package br.com.brunocarvalhs.core.analytics

import AnalyticsParam
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent

interface AnalyticsService {
    fun logEvent(name: AnalyticsEvent, params: Map<AnalyticsParam, Any?> = emptyMap())
    fun setUserProperty(name: String, value: String)
    fun setUserId(userId: String)
}
