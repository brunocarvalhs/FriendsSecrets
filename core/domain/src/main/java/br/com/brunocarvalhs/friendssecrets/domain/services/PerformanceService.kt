package br.com.brunocarvalhs.friendssecrets.domain.services

interface PerformanceService {
    fun start(simpleName: String)
    fun stop(simpleName: String)
}