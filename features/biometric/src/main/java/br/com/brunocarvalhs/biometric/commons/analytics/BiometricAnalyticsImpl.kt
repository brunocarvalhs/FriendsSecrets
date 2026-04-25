package br.com.brunocarvalhs.biometric.commons.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

class BiometricAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : BiometricAnalytics {
    override fun trackScreenView() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "BiometricScreen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "BiometricViewModel")
        }
    }

    override fun trackAuthenticationResult(success: Boolean, error: String?) {
        firebaseAnalytics.logEvent("biometric_authentication") {
            param("success", success.toString())
            error?.let { param("error", it) }
        }
    }
}
