package br.com.brunocarvalhs.friendssecrets.domain.services

interface ConfigurationService {
    fun getString(key: String, defaultValue: String): String
}
