package br.com.brunocarvalhs.core.notifications.domain

interface PushTokenRepository {
    suspend fun registerToken(groupId: String, deviceId: String): Result<Unit>
}
