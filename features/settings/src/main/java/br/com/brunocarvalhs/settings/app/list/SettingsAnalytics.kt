package br.com.brunocarvalhs.settings.app.list

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

internal interface SettingsAnalytics {
    fun trackScreenView()
    fun trackToggleBiometric(enabled: Boolean)
    fun trackFAQView()
    fun trackReportIssueView()
    fun trackAppearanceScreenView()
    fun trackChangeTheme(theme: String)
    fun trackToggleDynamicTheme(enabled: Boolean)
}

internal class SettingsAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : SettingsAnalytics {
    override fun trackScreenView() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "SettingsScreen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "SettingsViewModel")
        }
    }

    override fun trackToggleBiometric(enabled: Boolean) {
        firebaseAnalytics.logEvent("settings_toggle_biometric") {
            param("enabled", enabled.toString())
        }
    }

    override fun trackFAQView() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "FAQScreen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "FAQViewModel")
        }
    }

    override fun trackReportIssueView() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "ReportIssueScreen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ReportIssueViewModel")
        }
    }

    override fun trackAppearanceScreenView() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "AppearanceScreen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "AppearanceViewModel")
        }
    }

    override fun trackChangeTheme(theme: String) {
        firebaseAnalytics.logEvent("settings_change_theme") {
            param("theme", theme)
        }
    }

    override fun trackToggleDynamicTheme(enabled: Boolean) {
        firebaseAnalytics.logEvent("settings_toggle_dynamic_theme") {
            param("enabled", enabled.toString())
        }
    }
}
