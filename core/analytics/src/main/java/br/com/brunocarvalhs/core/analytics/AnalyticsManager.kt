package br.com.brunocarvalhs.core.analytics

import AnalyticsParam
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent
import br.com.brunocarvalhs.core.analytics.extensions.ConvertParameters
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsService {

    override fun logEvent(
        name: AnalyticsEvent,
        params: Map<AnalyticsParam, Any?>
    ) {
        val mappedParams: Map<String, Any?> = params.mapKeys { it.key.value }
        val bundle = ConvertParameters.toBundle(mappedParams)
        firebaseAnalytics.logEvent(name.value, bundle)
    }

    override fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name, value)
    }

    override fun setUserId(userId: String) {
        firebaseAnalytics.setUserId(userId)
    }
}