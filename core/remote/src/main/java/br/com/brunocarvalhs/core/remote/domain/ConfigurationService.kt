package br.com.brunocarvalhs.core.remote.domain

interface ConfigurationService {
    fun getString(key: String, defaultValue: String): String
}
