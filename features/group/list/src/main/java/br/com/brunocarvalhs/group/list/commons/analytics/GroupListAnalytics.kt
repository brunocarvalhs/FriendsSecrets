package br.com.brunocarvalhs.group.list.commons.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

internal interface GroupListAnalytics {
    fun trackScreenView()
    fun trackFetchGroups()
    fun trackGroupToEnter(token: String)
    fun trackSearch(query: String)
}

internal class GroupListAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : GroupListAnalytics {
    override fun trackScreenView() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "GroupListScreen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "GroupListViewModel")
        }
    }

    override fun trackFetchGroups() {
        firebaseAnalytics.logEvent("group_list_fetch", null)
    }

    override fun trackGroupToEnter(token: String) {
        firebaseAnalytics.logEvent("group_list_enter_by_token") {
            param("token", token)
        }
    }

    override fun trackSearch(query: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH) {
            param(FirebaseAnalytics.Param.SEARCH_TERM, query)
        }
    }
}
