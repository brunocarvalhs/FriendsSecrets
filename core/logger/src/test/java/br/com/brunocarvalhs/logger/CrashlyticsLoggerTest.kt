package br.com.brunocarvalhs.logger

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import timber.log.Timber
import java.util.concurrent.CancellationException

class CrashlyticsLoggerTest {

    private lateinit var crashlytics: FirebaseCrashlytics
    private lateinit var logger: CrashlyticsLogger

    @Before
    fun setup() {
        crashlytics = mockk(relaxed = true)
        logger = CrashlyticsLogger(crashlytics)

        Timber.plant(logger)
    }

    @After
    fun tearDown() {
        Timber.uprootAll()
    }

    @Test
    fun shouldIgnoreLowPriorityLogs() {
        Timber.v("verbose")
        Timber.d("debug")
        Timber.i("info")
        Timber.wtf("assert")

        verify { crashlytics wasNot Called }
    }

    @Test
    fun shouldLogWarningMessage() {
        val message = "warning message"

        Timber.w(message)

        verify(exactly = 1) { crashlytics.log(message) }
        verify(exactly = 0) { crashlytics.recordException(any()) }
    }

    @Test
    fun shouldRecordExceptionWhenErrorWithThrowable() {
        val throwable = RuntimeException("boom")

        Timber.e(throwable, "error occurred")

        verify(exactly = 1) { crashlytics.recordException(throwable) }
        verify(exactly = 0) { crashlytics.log(any()) }
    }

    @Test
    fun shouldCreateExceptionWhenErrorWithoutThrowable() {
        val message = "error message"

        Timber.e(message)

        verify {
            crashlytics.recordException(match {
                it is Exception && it.message == message
            })
        }
        verify(exactly = 0) { crashlytics.log(any()) }
    }

    @Test
    fun shouldIgnoreCancellationException() {
        Timber.e(CancellationException("job was cancelled"), "coroutine cancelled")

        verify(exactly = 0) { crashlytics.recordException(any()) }
        verify(exactly = 0) { crashlytics.log(any()) }
    }
}
