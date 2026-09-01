package br.com.brunocarvalhs.core.featureflags.domain

interface VersionedFeatureFlagService {
    /**
     * Returns whether the feature identified by [key] is enabled for the
     * app version currently running, based on a remote list of per-version
     * overrides. When no override targets the current version, [defaultValue]
     * is returned.
     */
    fun isEnabled(key: String, defaultValue: Boolean = true): Boolean
}
