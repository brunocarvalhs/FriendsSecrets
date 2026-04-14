package br.com.brunocarvalhs.biometric.app.presentation

import androidx.fragment.app.FragmentActivity
import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricResult
import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BiometricViewModelTest {

    private val biometricUseCase: BiometricUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should check if can authenticate`() {
        every { biometricUseCase.canAuthenticate() } returns true
        val viewModel = BiometricViewModel(biometricUseCase)
        assertTrue(viewModel.state.value.canAuthenticate)
    }

    @Test
    fun `handleIntent Authenticate should call authenticate and update state on success`() {
        val activity: FragmentActivity = mockk()
        every { biometricUseCase.canAuthenticate() } returns true
        every { biometricUseCase.authenticate(activity) } returns flowOf(BiometricResult.Success)
        val viewModel = BiometricViewModel(biometricUseCase)

        viewModel.handleIntent(BiometricIntent.Authenticate(activity))

        assertTrue(viewModel.state.value.isAuthenticated)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `handleIntent Authenticate should update state on failure`() {
        val activity: FragmentActivity = mockk(relaxed = true)
        every { activity.getString(any()) } returns "Failed attempt"
        every { biometricUseCase.canAuthenticate() } returns true
        every { biometricUseCase.authenticate(activity) } returns flowOf(BiometricResult.FailedAttempt)
        val viewModel = BiometricViewModel(biometricUseCase)

        viewModel.handleIntent(BiometricIntent.Authenticate(activity))

        assertFalse(viewModel.state.value.isAuthenticated)
        assertEquals("Failed attempt", viewModel.state.value.failedAttemptMessage)
    }

    @Test
    fun `handleIntent Authenticate should update state on error`() {
        val activity: FragmentActivity = mockk()
        val errorMessage = "Error message"
        every { biometricUseCase.canAuthenticate() } returns true
        every { biometricUseCase.authenticate(activity) } returns flowOf(BiometricResult.Error(1, errorMessage))
        val viewModel = BiometricViewModel(biometricUseCase)

        viewModel.handleIntent(BiometricIntent.Authenticate(activity))

        assertFalse(viewModel.state.value.isAuthenticated)
        assertEquals(errorMessage, viewModel.state.value.error)
    }

    @Test
    fun `handleIntent Authenticate should handle generic exception`() {
        val activity: FragmentActivity = mockk()
        val exceptionMessage = "Unexpected error"
        every { biometricUseCase.canAuthenticate() } returns true
        every { biometricUseCase.authenticate(activity) } returns flow { throw Exception(exceptionMessage) }
        val viewModel = BiometricViewModel(biometricUseCase)

        viewModel.handleIntent(BiometricIntent.Authenticate(activity))

        assertFalse(viewModel.state.value.isLoading)
        assertEquals(exceptionMessage, viewModel.state.value.error)
    }

    @Test
    fun `handleIntent Authenticate should handle cancellation exception`() {
        val activity: FragmentActivity = mockk()
        every { biometricUseCase.canAuthenticate() } returns true
        every { biometricUseCase.authenticate(activity) } returns flow { throw kotlinx.coroutines.CancellationException("Cancelled") }
        val viewModel = BiometricViewModel(biometricUseCase)

        viewModel.handleIntent(BiometricIntent.Authenticate(activity))

        assertFalse(viewModel.state.value.isLoading)
        assertEquals("Cancelled", viewModel.state.value.error)
    }

    @Test
    fun `handleIntent Authenticate should set isAuthenticated true if cannot authenticate`() {
        val activity: FragmentActivity = mockk()
        every { biometricUseCase.canAuthenticate() } returns false
        val viewModel = BiometricViewModel(biometricUseCase)

        viewModel.handleIntent(BiometricIntent.Authenticate(activity))

        assertTrue(viewModel.state.value.isAuthenticated)
    }
}
