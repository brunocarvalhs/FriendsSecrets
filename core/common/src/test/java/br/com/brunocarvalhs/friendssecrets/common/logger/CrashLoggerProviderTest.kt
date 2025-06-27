package br.com.brunocarvalhs.friendssecrets.common.logger

import android.util.Log
import br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics.CrashlyticsProvider
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import timber.log.Timber

class CrashLoggerProviderTest {

    private lateinit var crashlytics: CrashlyticsProvider
    private lateinit var crashLoggerProvider: CrashLoggerProvider

    @Before
    fun setUp() {
        crashlytics = mockk(relaxed = true)
        crashLoggerProvider = CrashLoggerProvider(crashlytics)
        Timber.uprootAll()
        Timber.plant(crashLoggerProvider)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Timber.uprootAll()
    }

    @Test
    fun `should ignore VERBOSE logs`() {
        crashLoggerProvider.log(Log.VERBOSE, "tag", "verbose message", null)
        verify { crashlytics wasNot Called }
    }

    @Test
    fun `should ignore DEBUG logs`() {
        crashLoggerProvider.log(Log.DEBUG, "tag", "debug message", null)
        verify { crashlytics wasNot Called }
    }

    @Test
    fun `should ignore INFO logs`() {
        crashLoggerProvider.log(Log.INFO, "tag", "info message", null)
        verify { crashlytics wasNot Called }
    }

    @Test
    fun `should ignore ASSERT logs`() {
        crashLoggerProvider.log(Log.ASSERT, "tag", "assert message", null)
        verify { crashlytics wasNot Called }
    }

    @Test
    fun `should call crashlytics log on WARN`() {
        val message = "warning message"
        crashLoggerProvider.log(Log.WARN, "tag", message, null)
        verify(exactly = 1) { crashlytics.log(any()) }
        verify(exactly = 0) { crashlytics.report(any()) }
    }

    @Test
    fun `should call crashlytics report on ERROR with throwable`() {
        val throwable = RuntimeException("error")
        crashLoggerProvider.log(Log.ERROR, "tag", "error message", throwable)
        verify(exactly = 1) { crashlytics.report(any()) }
        verify(exactly = 0) { crashlytics.log(any()) }
    }

    @Test
    fun `should call crashlytics report on ERROR without throwable`() {
        val message = "error without throwable"
        crashLoggerProvider.log(Log.ERROR, "tag", message, null)

        val slot = slot<Throwable>()
        verify(exactly = 1) { crashlytics.report(throwable = capture(slot)) }
        verify(exactly = 0) { crashlytics.log(any()) }
    }
}
