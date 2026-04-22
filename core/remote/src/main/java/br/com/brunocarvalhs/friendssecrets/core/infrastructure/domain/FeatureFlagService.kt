package br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain

interface FeatureFlagService {
    fun validate(key: String): Boolean
    fun validate(key: String, defaultValue: Boolean): Boolean
}