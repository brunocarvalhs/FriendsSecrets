package br.com.brunocarvalhs.group.details.commons.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

internal class GroupDetailsAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : GroupDetailsAnalytics {
    override fun trackScreenView() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "GroupDetailsScreen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "GroupDetailsViewModel")
        }
    }

    override fun trackRefreshGroup() {
        firebaseAnalytics.logEvent("group_details_refresh", null)
    }

    override fun trackDeleteGroup() {
        firebaseAnalytics.logEvent("group_details_delete", null)
    }

    override fun trackExitGroup() {
        firebaseAnalytics.logEvent("group_details_exit", null)
    }

    override fun trackShareGroup() {
        firebaseAnalytics.logEvent("group_details_share", null)
    }
}
