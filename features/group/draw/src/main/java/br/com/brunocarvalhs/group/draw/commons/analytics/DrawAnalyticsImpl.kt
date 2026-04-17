package br.com.brunocarvalhs.group.draw.commons.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

internal class DrawAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : DrawAnalytics {
    override fun trackDrawAction() {
        firebaseAnalytics.logEvent("draw_action", null)
    }

    override fun trackShareAction() {
        firebaseAnalytics.logEvent("share_secret_action", null)
    }

    override fun trackScreenView() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "DrawScreen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "DrawViewModel")
        }
    }
}