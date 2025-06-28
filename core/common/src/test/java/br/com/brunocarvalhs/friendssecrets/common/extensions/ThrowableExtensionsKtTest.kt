package br.com.brunocarvalhs.friendssecrets.common.extensions

import br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics.CrashlyticsProvider
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

class ThrowableExtensionsTest {

    private val crashlyticsMock = mockk<CrashlyticsProvider>(relaxed = true)

    @Before
    fun setUp() {
        mockkObject(CrashlyticsProvider)  // mocka o objeto singleton
        every { CrashlyticsProvider.getInstance() } returns crashlyticsMock
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `report should call Crashlytics report when throwable is not null`() {
        val throwable = RuntimeException("Test Exception")
        val params = mapOf("key" to "value")

        val returned = throwable.report(params)

        verify(exactly = 1) { crashlyticsMock.report(throwable, params) }
        assertSame(throwable, returned)
    }

    @Test
    fun `report should return null and not call Crashlytics when throwable is null`() {
        val throwable: Throwable? = null

        val returned = throwable.report()

        verify(exactly = 0) { crashlyticsMock.report(any(), any()) }
        assertNull(returned)
    }
}
