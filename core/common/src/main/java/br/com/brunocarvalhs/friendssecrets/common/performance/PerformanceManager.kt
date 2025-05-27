package br.com.brunocarvalhs.friendssecrets.common.performance

import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import timber.log.Timber

class PerformanceManager(
    private val event: PerformanceEvent
) : PerformanceService {

    override fun start(simpleName: String) = event.start(simpleName)

    override fun stop(simpleName: String) = event.stop(simpleName)

    internal inline fun <T> trace(name: String, block: () -> T): T {
        event.start(name)
        try {
            return block()
        } finally {
            event.stop(name)
        }
    }

    interface PerformanceEvent {
        fun start(name: String)
        fun stop(name: String)
    }

    companion object {
        @Volatile
        private var instance: PerformanceManager? = null

        fun initialize(event: PerformanceEvent) {
            synchronized(this) {
                if (instance == null) {
                    instance = PerformanceManager(event)
                } else {
                    Timber.tag("PerformanceManager").w("Provider já inicializado.")
                }
            }
        }

        fun getInstance(): PerformanceManager {
            return instance ?: synchronized(this) {
                instance
                    ?: throw IllegalStateException("PerformanceManager não inicializado. Chame initialize() primeiro.")
            }
        }
    }
}