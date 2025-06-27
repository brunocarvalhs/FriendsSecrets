package br.com.brunocarvalhs.friendssecrets.common.performance

import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService

class PerformanceManager(
    private val event: PerformanceEvent
) : PerformanceService {

    init {
        instance = this
    }

    override fun start(simpleName: String) = event.start(simpleName)

    override fun stop(simpleName: String) = event.stop(simpleName)

    override fun parameter(key: String, value: String) = event.parameter(key, value)

    internal inline fun <reified T> trace(name: String, block: (Class<T>) -> T): T {
        event.start(name)
        try {
            return block(T::class.java)
        } finally {
            event.stop(name)
        }
    }

    interface PerformanceEvent {
        fun start(name: String)
        fun stop(name: String)
        fun setDeviceId(id: String)
        fun parameter(key: String, value: String)
    }

    companion object {
        @Volatile
        private var instance: PerformanceManager? = null

        @JvmStatic
        fun getInstance(): PerformanceManager {
            return instance ?: throw IllegalStateException("PerformanceManager not initialized")
        }

        @JvmStatic
        fun initialize(event: PerformanceEvent) {
            instance = PerformanceManager(event)
        }
    }
}