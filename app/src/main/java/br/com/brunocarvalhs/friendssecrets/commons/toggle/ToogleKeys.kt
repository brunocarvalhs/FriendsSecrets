package br.com.brunocarvalhs.friendssecrets.commons.toggle

enum class ToggleKeys {
    SETTINGS_IS_ENABLED,
    SETTINGS_IS_FINGERPRINT_ENABLED,
    SETTINGS_IS_APPEARANCE_ENABLED,
    SETTINGS_IS_REPORT_ISSUE_ENABLED,
    SETTINGS_IS_FAQ_ENABLED;

    override fun toString(): String {
        return name.lowercase()
    }
}