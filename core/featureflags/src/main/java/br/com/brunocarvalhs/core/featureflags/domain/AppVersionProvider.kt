package br.com.brunocarvalhs.core.featureflags.domain

interface AppVersionProvider {
    /** The running app's versionName, e.g. "3.7.0". Empty if it cannot be resolved. */
    fun getVersionName(): String
}
