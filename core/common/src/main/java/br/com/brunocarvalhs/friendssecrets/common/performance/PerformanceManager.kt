package br.com.brunocarvalhs.friendssecrets.common.performance

import timber.log.Timber

class PerformanceManager(
    private val event: PerformanceEvent
) {

    fun start(name: String) = event.start(name)

    fun stop(name: String) = event.stop(name)

    fun parameter(key: String, value: String) = event.parameter(key, value)

    internal inline fun <T> trace(name: String, block: (PerformanceEvent) -> T): T {
        event.start(name)
        try {
            return block(event)
        } finally {
            event.stop(name)
        }
    }

    interface PerformanceEvent {
        fun start(name: String)
        fun stop(name: String)
        fun parameter(key: String, value: String)
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