package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.core.notifications.domain.PushTokenRepository
import br.com.brunocarvalhs.deviceid.DeviceService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RegisterGroupPushTokenUseCaseTest {

    private val pushTokenRepository: PushTokenRepository = mockk()
    private val deviceService: DeviceService = mockk()
    private lateinit var useCase: RegisterGroupPushTokenUseCase

    @Before
    fun setup() {
        useCase = RegisterGroupPushTokenUseCase(pushTokenRepository, deviceService)
    }

    @Test
    fun `invoke should register the token for the current device and group`() = runTest {
        // Given
        coEvery { deviceService.getDeviceId() } returns "device-1"
        coEvery { pushTokenRepository.registerToken("group-1", "device-1") } returns Result.success(Unit)

        // When
        val result = useCase("group-1")

        // Then
        assertTrue(result.isSuccess)
        coVerify { pushTokenRepository.registerToken("group-1", "device-1") }
    }
}
