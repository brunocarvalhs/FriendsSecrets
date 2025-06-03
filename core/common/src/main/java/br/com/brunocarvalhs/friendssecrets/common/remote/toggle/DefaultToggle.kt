package br.com.brunocarvalhs.friendssecrets.common.remote.toggle

import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleKeys.APP_IS_THEME_REMOTE
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleKeys.DRAW_IS_GENERATIVE_ENABLED
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleKeys.HOME_IS_CREATE_GROUP_ENABLED
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleKeys.HOME_IS_JOIN_GROUP_ENABLED
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleKeys.SETTINGS_IS_APPEARANCE_ENABLED
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleKeys.SETTINGS_IS_ENABLED
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleKeys.SETTINGS_IS_FAQ_ENABLED
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleKeys.SETTINGS_IS_FINGERPRINT_ENABLED
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleKeys.SETTINGS_IS_REPORT_ISSUE_ENABLED

internal val defaultToggle = mapOf(
    // App
    APP_IS_THEME_REMOTE.toString() to true,
    // Draw
    DRAW_IS_GENERATIVE_ENABLED.toString() to false,
    // Home
    HOME_IS_CREATE_GROUP_ENABLED.toString() to true,
    HOME_IS_JOIN_GROUP_ENABLED.toString() to true,

    // Settings
    SETTINGS_IS_ENABLED.toString() to true,
    SETTINGS_IS_FINGERPRINT_ENABLED.toString() to true,
    SETTINGS_IS_APPEARANCE_ENABLED.toString() to true,
    SETTINGS_IS_REPORT_ISSUE_ENABLED.toString() to false,
    SETTINGS_IS_FAQ_ENABLED.toString() to false
)