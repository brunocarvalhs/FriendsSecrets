package br.com.brunocarvalhs.friendssecrets.commons.logger

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import timber.log.Timber

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class, manifest = Config.NONE)
class CrashLoggerProviderTest {

    private val crashlytics: FirebaseCrashlytics = mockk(relaxed = true) {
        every { recordException(any()) } just Runs
        every { log(any()) } just Runs
    }
    private lateinit var crashLoggerProvider: CrashLoggerProvider

    @Before
    fun setup() {
        crashLoggerProvider = CrashLoggerProvider(crashlytics)
        Timber.plant(crashLoggerProvider)
    }

    @After
    fun tearDown() {
        Timber.uproot(crashLoggerProvider)
    }

    @Test
    fun `log should ignore VERBOSE DEBUG INFO and ASSERT priorities`() {
        // When
        Timber.tag("TAG").v("message")
        Timber.tag("TAG").d("message")
        Timber.tag("TAG").i("message")

        // Then
        verify(exactly = 0) { crashlytics.recordException(any()) }
        verify(exactly = 0) { crashlytics.log(any()) }
    }

    @Test
    fun `log should record exception for ERROR priority`() {
        // Given
        val exception = RuntimeException("Crash!")
        val message = "Error message"
        
        // When
        Timber.tag("TAG").e(exception, message)

        // Then
        verify(exactly = 1) { crashlytics.recordException(exception) }
    }

    @Test
    fun `log should log message for WARN priority`() {
        // Given
        val message = "Warning message"

        // When
        Timber.tag("TAG").w(message)

        // Then
        verify(exactly = 1) { crashlytics.log(message) }
    }
}
