package br.com.brunocarvalhs.friendssecrets.common.analytics

import android.os.Bundle
import io.mockk.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AnalyticsProviderTest {

    private val event = mockk<AnalyticsProvider.AnalyticsEvent>(relaxed = true)

    @Before
    fun setUp() {
        // Reseta a instância singleton antes de cada teste
        val instanceField = AnalyticsProvider::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test(expected = IllegalStateException::class)
    fun `getInstance throws if not initialized`() {
        AnalyticsProvider.getInstance() // deve lançar IllegalStateException
    }

    @Test
    fun `initialize sets instance and getInstance returns it`() {
        AnalyticsProvider.initialize(event)
        val instance = AnalyticsProvider.getInstance()
        assertNotNull(instance)
        assertSame(instance, AnalyticsProvider.getInstance()) // singleton mesma instância
    }

    @Test
    fun `track calls logEvent with correct parameters`() {
        AnalyticsProvider.initialize(event)
        val analytics = AnalyticsProvider.getInstance()

        val params = mapOf(
            AnalyticsParams.USER_ID to "user123",
            AnalyticsParams.USER_ACTION to "click"
        )

        analytics.track(AnalyticsEvents.CLICK, params)

        val slotBundle = slot<Bundle>()
        verify(exactly = 1) {
            event.logEvent(AnalyticsEvents.CLICK.value, capture(slotBundle))
        }

        val bundle = slotBundle.captured
        assertEquals("user123", bundle.getString(AnalyticsParams.USER_ID.value))
        assertEquals("click", bundle.getString(AnalyticsParams.USER_ACTION.value))
    }
}
