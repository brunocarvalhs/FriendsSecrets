package br.com.brunocarvalhs.friendssecrets.domain.services

interface FeatureFlagService {
    fun validate(key: String): Boolean
    fun validate(key: String, defaultValue: Boolean): Boolean
}
