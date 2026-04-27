package br.com.brunocarvalhs.core.remote.domain

interface FeatureFlagService {
    fun validate(key: String): Boolean
    fun validate(key: String, defaultValue: Boolean): Boolean
}
