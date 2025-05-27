package br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics

import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import timber.log.Timber

class CrashlyticsProviderTest {

    private lateinit var event: CrashlyticsProvider.CrashlyticsEvent
    private lateinit var provider: CrashlyticsProvider

    @Before
    fun setUp() {
        event = mockk(relaxed = true)
        CrashlyticsProvider.initialize(event)
        provider = CrashlyticsProvider.getInstance()
        Timber.uprootAll()
    }

    @After
    fun tearDown() {
        clearAllMocks()
        val field = CrashlyticsProvider::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)
    }

    @Test
    fun `report calls event report and sets parameters`() {
        val throwable = RuntimeException("test exception")
        val params = mapOf("key1" to "value1", "key2" to "value2")

        provider.report(throwable, params)

        params.forEach { (k, v) ->
            verify(exactly = 1) { event.parameter(k, v) }
        }
        verify(exactly = 1) { event.report(throwable) }
    }

    @Test
    fun `report calls event report without parameters`() {
        val throwable = RuntimeException("no params")

        provider.report(throwable)

        verify(exactly = 0) { event.parameter(any(), any()) }
        verify(exactly = 1) { event.report(throwable) }
    }

    @Test
    fun `log calls event log and sets parameters`() {
        val message = "log message"
        val params = mapOf("keyA" to "valueA")

        provider.log(message, params)

        params.forEach { (k, v) ->
            verify(exactly = 1) { event.parameter(k, v) }
        }
        verify(exactly = 1) { event.log(message) }
    }

    @Test
    fun `log calls event log without parameters`() {
        val message = "simple log"

        provider.log(message)

        verify(exactly = 0) { event.parameter(any(), any()) }
        verify(exactly = 1) { event.log(message) }
    }

    @Test
    fun `getInstance throws if not initialized`() {
        val field = CrashlyticsProvider::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        val exception = Assert.assertThrows(IllegalStateException::class.java) {
            CrashlyticsProvider.getInstance()
        }
        assertEquals(
            "CrashlyticsProvider não inicializado. Chame initialize() primeiro.",
            exception.message
        )
    }

    @Test
    fun `initialize only initializes once`() {
        CrashlyticsProvider.initialize(event)
        verify(exactly = 0) { event.report(any()) }
    }
}
