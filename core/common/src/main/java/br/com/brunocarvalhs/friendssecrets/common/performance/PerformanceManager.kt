package br.com.brunocarvalhs.friendssecrets.common.performance

import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService

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
        fun setDeviceId(id: String)
    }
}