package br.com.brunocarvalhs.core.notifications.data

import br.com.brunocarvalhs.core.network.domain.NetworkRequest
import br.com.brunocarvalhs.core.network.domain.NetworkService
import br.com.brunocarvalhs.core.notifications.domain.PushTokenRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class PushTokenRepositoryImpl @Inject constructor(
    private val network: NetworkService,
    private val messaging: FirebaseMessaging,
) : PushTokenRepository {

    override suspend fun registerToken(groupId: String, deviceId: String): Result<Unit> =
        runCatching {
            val token = messaging.token.await()

            // FirebaseFirestoreManager.execute() only understands flat two-segment
            // "collection/documentId" endpoints (no nested subcollections), so the
            // document id itself encodes the (groupId, deviceId) pair. The Cloud
            // Function side queries this collection filtered by the groupId field.
            network.make(
                request = NetworkRequest(
                    endpoint = "$PUSH_TOKENS_COLLECTION/${groupId}_$deviceId",
                    payload = mapOf(
                        "groupId" to groupId,
                        "deviceId" to deviceId,
                        "token" to token,
                        "updatedAt" to System.currentTimeMillis()
                    ),
                    method = NetworkService.Method.PUT
                ),
                response = PushTokenDTO::class
            )

            Unit
        }

    private companion object {
        const val PUSH_TOKENS_COLLECTION = "push_tokens"
    }
}
