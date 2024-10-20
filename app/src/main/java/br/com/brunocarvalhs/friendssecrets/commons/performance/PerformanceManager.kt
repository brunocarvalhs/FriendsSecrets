package br.com.brunocarvalhs.friendssecrets.commons.performance

import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.ktx.performance

class PerformanceManager(
    private val firebasePerformance: FirebasePerformance = Firebase.performance
) {
    fun start(name: String) {
        firebasePerformance.newTrace(name).start()
    }

    fun stop(name: String) {
        firebasePerformance.newTrace(name).stop()
    }
}