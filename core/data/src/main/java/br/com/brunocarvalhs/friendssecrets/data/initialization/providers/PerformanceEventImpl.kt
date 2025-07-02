package br.com.brunocarvalhs.friendssecrets.data.initialization.providers

import br.com.brunocarvalhs.friendssecrets.common.performance.PerformanceManager
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.AddTrace
import com.google.firebase.perf.metrics.Trace
import dagger.Lazy


class PerformanceEventImpl(
    private val firebasePerformance: Lazy<FirebasePerformance>
) : PerformanceManager.PerformanceEvent {

    private val performance by lazy { firebasePerformance.get() }

    private val traces = mutableMapOf<String, Trace>()

    @AddTrace(name = "PerformanceEventImpl.start", enabled = true)
    override fun start(name: String) {
        traces[name] = performance.newTrace(name)
        traces[name]?.start()
    }

    @AddTrace(name = "PerformanceEventImpl.stop", enabled = true)
    override fun stop(name: String) {
        traces[name]?.stop()
    }

    @AddTrace(name = "PerformanceEventImpl.setDeviceId", enabled = true)
    override fun setDeviceId(id: String) {
        performance.putAttribute("deviceId", id)
    }

    @AddTrace(name = "PerformanceEventImpl.putAttribute", enabled = true)
    override fun parameter(key: String, value: String) {
        performance.putAttribute(key, value)
    }
}