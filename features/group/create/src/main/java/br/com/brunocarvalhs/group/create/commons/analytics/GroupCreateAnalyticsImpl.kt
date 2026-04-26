package br.com.brunocarvalhs.group.create.commons.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

internal class GroupCreateAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : GroupCreateAnalytics {
    override fun trackFormsScreenView() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "FormsScreen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "FormsViewModel")
        }
    }

    override fun trackContactsScreenView() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "ContactsScreen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ContactsViewModel")
        }
    }

    override fun trackEditFormScreenView() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "EditFormScreen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "EditFormsViewModel")
        }
    }

    override fun trackCreateGroup(success: Boolean) {
        firebaseAnalytics.logEvent("group_create_finish") {
            param("success", success.toString())
        }
    }

    override fun trackAddMember() {
        firebaseAnalytics.logEvent("group_create_add_member", null)
    }

    override fun trackSelectContact() {
        firebaseAnalytics.logEvent("group_create_select_contact", null)
    }
}
