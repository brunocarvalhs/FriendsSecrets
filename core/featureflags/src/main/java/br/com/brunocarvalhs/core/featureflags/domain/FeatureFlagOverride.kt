package br.com.brunocarvalhs.core.featureflags.domain

import kotlinx.serialization.Serializable

/**
 * A remote kill-switch for a single feature, scoped to one app version.
 *
 * The override is only applied when the running app's versionName matches
 * [version] exactly, so a buggy release can be disabled remotely without
 * affecting versions where the bug has already been fixed.
 */
@Serializable
data class FeatureFlagOverride(
    val key: String,
    val enabled: Boolean,
    val version: String
)
