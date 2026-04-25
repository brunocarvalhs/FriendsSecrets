package br.com.brunocarvalhs.biometric.app.presentation

import androidx.fragment.app.FragmentActivity
import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricResult
import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricUseCase
import br.com.brunocarvalhs.biometric.commons.analytics.BiometricAnalytics
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BiometricViewModelTest {

    private val biometricUseCase: BiometricUseCase = mockk()
    private val biometricAnalytics: BiometricAnalytics = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: BiometricViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { biometricUseCase.canAuthenticate() } returns true
        viewModel = BiometricViewModel(biometricUseCase, biometricAnalytics)
        every { biometricAnalytics.trackScreenView() } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should check if can authenticate`() {
        // Then
        assertTrue(viewModel.state.value.canAuthenticate)
    }

    @Test
    fun `handleIntent Authenticate should update state when success`() = runTest {
        // Given
        val activity = mockk<FragmentActivity>(relaxed = true)
        every { biometricUseCase.authenticate(activity) } returns flowOf(BiometricResult.Success)

        // When
        viewModel.handleIntent(BiometricIntent.Authenticate(activity))

        // Then
        assertTrue(viewModel.state.value.isAuthenticated)
        assertEquals(false, viewModel.state.value.isLoading)
    }

    @Test
    fun `handleIntent Authenticate should update state when error`() = runTest {
        // Given
        val activity = mockk<FragmentActivity>(relaxed = true)
        val errorMessage = "Error message"
        every { biometricUseCase.authenticate(activity) } returns flowOf(BiometricResult.Error(1, errorMessage))

        // When
        viewModel.handleIntent(BiometricIntent.Authenticate(activity))

        // Then
        assertEquals(errorMessage, viewModel.state.value.error)
        assertEquals(false, viewModel.state.value.isLoading)
    }

    @Test
    fun `handleIntent Authenticate should set isAuthenticated true if canAuthenticate is false`() {
        // Given
        every { biometricUseCase.canAuthenticate() } returns false
        val viewModelNoBio = BiometricViewModel(biometricUseCase)
        val activity = mockk<FragmentActivity>(relaxed = true)

        // When
        viewModelNoBio.handleIntent(BiometricIntent.Authenticate(activity))

        // Then
        assertTrue(viewModelNoBio.state.value.isAuthenticated)
    }
}
