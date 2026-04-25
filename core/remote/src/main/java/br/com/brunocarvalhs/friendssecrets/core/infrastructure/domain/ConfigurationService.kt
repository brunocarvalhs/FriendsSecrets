package br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain

interface ConfigurationService {
    fun getString(key: String, defaultValue: String): String
}
