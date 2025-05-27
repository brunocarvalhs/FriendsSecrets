package br.com.brunocarvalhs.friendssecrets.initialization.providers

import br.com.brunocarvalhs.friendssecrets.common.performance.PerformanceManager
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace


class PerformanceEventImpl(
    private val firebasePerformance: FirebasePerformance
) : PerformanceManager.PerformanceEvent {

    private val traces = mutableMapOf<String, Trace>()

    override fun start(name: String) {
        traces[name] = firebasePerformance.newTrace(name)
        traces[name]?.start()
    }

    override fun stop(name: String) {
        traces[name]?.stop()
    }
}