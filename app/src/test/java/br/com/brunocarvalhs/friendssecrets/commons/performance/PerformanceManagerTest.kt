package br.com.brunocarvalhs.friendssecrets.commons.performance

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class PerformanceManagerTest {
    private lateinit var firebasePerformance: FirebasePerformance
    private lateinit var performanceManager: PerformanceManager

    @Before
    fun setUp() {
        firebasePerformance = mockk()
        performanceManager = PerformanceManager(firebasePerformance)
    }

    @Test
    fun `test start`() {
        val trace = mockk<Trace>(relaxed = true)
        every { firebasePerformance.newTrace("testTrace") } returns trace

        performanceManager.start("testTrace")

        verify { trace.start() }
    }

    @Test
    fun `test stop`() {
        val trace = mockk<Trace>(relaxed = true)
        every { firebasePerformance.newTrace("testTrace") } returns trace

        performanceManager.stop("testTrace")

        verify { trace.stop() }
    }
}