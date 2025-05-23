package br.com.brunocarvalhs.friendssecrets.commons.performance

import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.ktx.performance
import com.google.firebase.perf.metrics.AddTrace
import com.google.firebase.perf.metrics.Trace

class PerformanceManager(
    private val firebasePerformance: FirebasePerformance = Firebase.performance
) {

    private val traces = mutableMapOf<String, Trace>()

    fun start(name: String) {
        val trace = firebasePerformance.newTrace(name)
        trace.start()
        traces[name] = trace
    }

    fun stop(name: String) {
        traces.remove(name)?.stop()
    }

    internal inline fun <T> trace(name: String, block: (Trace) -> T): T {
        val trace = this.firebasePerformance.newTrace(name)
        trace.start()
        try {
            return block(trace)
        } finally {
            trace.stop()
        }
    }
}