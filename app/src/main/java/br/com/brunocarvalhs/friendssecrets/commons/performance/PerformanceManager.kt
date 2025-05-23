package br.com.brunocarvalhs.friendssecrets.commons.performance

import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.ktx.performance
import com.google.firebase.perf.metrics.AddTrace

class PerformanceManager(
    private val firebasePerformance: FirebasePerformance = Firebase.performance
) {
    @AddTrace(name = "PerformanceManager.start")
    fun start(name: String) {
        firebasePerformance.newTrace(name).start()
    }

    @AddTrace(name = "PerformanceManager.stop")
    fun stop(name: String) {
        firebasePerformance.newTrace(name).stop()
    }
}