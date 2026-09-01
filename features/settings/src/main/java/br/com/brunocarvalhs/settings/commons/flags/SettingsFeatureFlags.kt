package br.com.brunocarvalhs.settings.commons.flags

import br.com.brunocarvalhs.core.remote.domain.FeatureFlagService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SettingsFeatureFlags @Inject constructor(
    private val service: FeatureFlagService
) {
    fun isAppearanceEnabled(): Boolean = service.validate(FEATURE_SETTINGS_APPEARANCE, true)
    fun isFingerprintEnabled(): Boolean = service.validate(FEATURE_SETTINGS_FINGERPRINT, true)
    fun isReportIssueEnabled(): Boolean = service.validate(FEATURE_SETTINGS_REPORT_ISSUE, true)
    fun isFaqEnabled(): Boolean = service.validate(FEATURE_SETTINGS_FAQ, true)
}

private const val FEATURE_SETTINGS_APPEARANCE = "settings_is_appearance_enabled"
private const val FEATURE_SETTINGS_FINGERPRINT = "settings_is_fingerprint_enabled"
private const val FEATURE_SETTINGS_REPORT_ISSUE = "settings_is_report_issue_enabled"
private const val FEATURE_SETTINGS_FAQ = "settings_is_faq_enabled"
