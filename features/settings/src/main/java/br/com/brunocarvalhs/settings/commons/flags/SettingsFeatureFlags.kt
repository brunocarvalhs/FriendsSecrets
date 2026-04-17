package br.com.brunocarvalhs.settings.commons.flags

import br.com.brunocarvalhs.friendssecrets.domain.services.FeatureFlagService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SettingsFeatureFlags @Inject constructor(
    private val service: FeatureFlagService
) {
    fun isSettingsEnabled(): Boolean = service.validate(FEATURE_SETTINGS, true)
    fun isReportIssueEnabled(): Boolean = service.validate(FEATURE_SETTINGS_REPORT_ISSUE, true)
    fun isFaqEnabled(): Boolean = service.validate(FEATURE_SETTINGS_FAQ, true)
}

private const val FEATURE_SETTINGS = "feature_settings_enabled"
private const val FEATURE_SETTINGS_REPORT_ISSUE = "feature_settings_report_issue_enabled"
private const val FEATURE_SETTINGS_FAQ = "feature_settings_faq_enabled"
