package br.com.brunocarvalhs.friendssecrets.core.analytics

import br.com.brunocarvalhs.friendssecrets.core.analytics.extensions.ConvertParameters
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsService {

    override fun logEvent(name: String, params: Map<String, Any?>) {
        val bundle = ConvertParameters.toBundle(params)
        firebaseAnalytics.logEvent(name, bundle)
    }

    override fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name, value)
    }

    override fun setUserId(userId: String) {
        firebaseAnalytics.setUserId(userId)
    }
}
