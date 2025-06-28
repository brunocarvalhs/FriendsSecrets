package br.com.brunocarvalhs.settings.commons.toggles

import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleKeys
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleManager

internal fun getToggles(manager: ToggleManager): Map<ToggleKeys, Boolean> = mapOf(
    ToggleKeys.SETTINGS_IS_FINGERPRINT_ENABLED to true,
    ToggleKeys.SETTINGS_IS_APPEARANCE_ENABLED to true,
    ToggleKeys.SETTINGS_IS_REPORT_ISSUE_ENABLED to true,
)