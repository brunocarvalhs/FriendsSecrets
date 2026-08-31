package br.com.brunocarvalhs.group.details.app.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.deviceid.DeviceService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ShareWishlistUseCaseTest {

    private val context: Context = mockk(relaxed = true)
    private val deviceService: DeviceService = mockk()
    private lateinit var useCase: ShareWishlistUseCase

    @Before
    fun setup() {
        every { context.getString(any(), *anyVararg()) } returns "message"
        useCase = ShareWishlistUseCase(context, deviceService)
    }

    @Test
    fun `invoke should start share intent when current member has likes`() = runTest {
        // Given
        val self = UserModel(id = "device-1", name = "Bruno", likes = listOf("Books", "Games"))
        val group = GroupModel(id = "group-1", name = "Family", members = listOf(self))
        coEvery { deviceService.getDeviceId() } returns "device-1"

        // When
        val result = useCase(group)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke should fail when current member has no likes`() = runTest {
        // Given
        val self = UserModel(id = "device-1", name = "Bruno", likes = emptyList())
        val group = GroupModel(id = "group-1", members = listOf(self))
        coEvery { deviceService.getDeviceId() } returns "device-1"

        // When
        val result = useCase(group)

        // Then
        assertTrue(result.exceptionOrNull() is ShareWishlistUseCase.EmptyWishlistException)
    }

    @Test
    fun `invoke should fail when current device is not a member`() = runTest {
        // Given
        val other = UserModel(id = "device-2", name = "Isabella", likes = listOf("Music"))
        val group = GroupModel(id = "group-1", members = listOf(other))
        coEvery { deviceService.getDeviceId() } returns "device-1"

        // When
        val result = useCase(group)

        // Then
        assertTrue(result.exceptionOrNull() is ShareWishlistUseCase.MemberNotFoundException)
    }
}
