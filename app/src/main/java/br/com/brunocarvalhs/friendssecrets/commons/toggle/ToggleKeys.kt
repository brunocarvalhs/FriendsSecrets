package br.com.brunocarvalhs.friendssecrets.commons.toggle

enum class ToggleKeys {
    DRAW_IS_GENERATIVE_ENABLED,
    HOME_IS_JOIN_GROUP_ENABLED,
    HOME_IS_CREATE_GROUP_ENABLED,
    SETTINGS_IS_ENABLED,
    SETTINGS_IS_FINGERPRINT_ENABLED,
    SETTINGS_IS_APPEARANCE_ENABLED,
    SETTINGS_IS_REPORT_ISSUE_ENABLED,
    SETTINGS_IS_FAQ_ENABLED;

    override fun toString(): String {
        return name.lowercase()
    }
}
