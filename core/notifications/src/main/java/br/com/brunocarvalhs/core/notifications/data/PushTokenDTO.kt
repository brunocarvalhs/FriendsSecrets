package br.com.brunocarvalhs.core.notifications.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PushTokenDTO(
    @SerialName("groupId") val groupId: String = "",
    @SerialName("deviceId") val deviceId: String = "",
    @SerialName("token") val token: String = "",
    @SerialName("updatedAt") val updatedAt: Long = 0L,
)
