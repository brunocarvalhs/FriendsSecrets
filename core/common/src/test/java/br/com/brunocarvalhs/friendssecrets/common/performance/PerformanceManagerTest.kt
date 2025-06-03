package br.com.brunocarvalhs.friendssecrets.common.performance

import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PerformanceManagerTest {

    private lateinit var event: PerformanceManager.PerformanceEvent
    private lateinit var manager: PerformanceManager

    @Before
    fun setup() {
        event = mockk(relaxed = true)
        PerformanceManager.initialize(event)
        manager = PerformanceManager.getInstance()
    }

    @After
    fun tearDown() {
        clearAllMocks()
        val field = PerformanceManager::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)
    }

    @Test(expected = IllegalStateException::class)
    fun `getInstance throws if not initialized`() {
        // Clear instance for test
        val field = PerformanceManager::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        PerformanceManager.getInstance()
    }

    @Test
    fun `initialize creates instance only once`() {
        PerformanceManager.initialize(event) // second call, should log warning but no crash
        val instance1 = PerformanceManager.getInstance()
        val instance2 = PerformanceManager.getInstance()
        assertEquals(instance1, instance2)
    }

    @Test
    fun `start delegates to event`() {
        val name = "testStart"
        manager.start(name)
        verify { event.start(name) }
    }

    @Test
    fun `stop delegates to event`() {
        val name = "testStop"
        manager.stop(name)
        verify { event.stop(name) }
    }

    @Test
    fun `parameter delegates to event`() {
        val key = "key"
        val value = "value"
        manager.parameter(key, value)
        verify { event.parameter(key, value) }
    }

    @Test
    fun `trace starts, executes block and stops`() {
        val name = "traceTest"
        var executed = false

        val result = manager.trace(name) { perfEvent ->
            verify { event.start(name) }
            executed = true
            "result"
        }

        verify { event.stop(name) }
        assertEquals("result", result)
        assertEquals(true, executed)
    }

    @Test
    fun `trace stops even if block throws`() {
        val name = "traceTestException"

        try {
            manager.trace<String>(name) { _ ->
                verify { event.start(name) }
                throw RuntimeException("fail")
            }
        } catch (e: RuntimeException) {
            // expected
        }

        verify { event.stop(name) }
    }
}
