package br.com.brunocarvalhs.core.notifications.data

import br.com.brunocarvalhs.core.network.domain.NetworkRequest
import br.com.brunocarvalhs.core.network.domain.NetworkService
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
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
class PushTokenRepositoryImplTest {

    private val network: NetworkService = mockk()
    private val messaging: FirebaseMessaging = mockk()
    private lateinit var repository: PushTokenRepositoryImpl

    @Before
    fun setup() {
        repository = PushTokenRepositoryImpl(network, messaging)
    }

    @Test
    fun `registerToken should write the current fcm token under the group's push_tokens`() = runTest {
        // Given
        every { messaging.token } returns Tasks.forResult("fcm-token-123")
        coEvery {
            network.make(
                request = match {
                    it.endpoint == "push_tokens/group-1_device-1" &&
                        it.method == NetworkService.Method.PUT &&
                        it.payload?.get("groupId") == "group-1" &&
                        it.payload?.get("deviceId") == "device-1" &&
                        it.payload?.get("token") == "fcm-token-123"
                },
                response = PushTokenDTO::class
            )
        } returns PushTokenDTO(groupId = "group-1", deviceId = "device-1", token = "fcm-token-123")

        // When
        val result = repository.registerToken(groupId = "group-1", deviceId = "device-1")

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `registerToken should fail when fetching the fcm token throws`() = runTest {
        // Given
        every { messaging.token } returns Tasks.forException(IllegalStateException("no token"))

        // When
        val result = repository.registerToken(groupId = "group-1", deviceId = "device-1")

        // Then
        assertTrue(result.isFailure)
    }
}
