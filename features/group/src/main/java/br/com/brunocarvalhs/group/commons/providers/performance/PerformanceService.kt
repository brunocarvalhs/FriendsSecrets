package br.com.brunocarvalhs.group.commons.providers.performance

interface PerformanceService {
    fun start(simpleName: String)
    fun stop(simpleName: String)
    fun parameter(key: String, value: String)
}