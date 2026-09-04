package br.com.brunocarvalhs.core.notifications.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class GroupSyncState(
    val hasDraw: Boolean = false,
    val lastMessageTimestamp: Long = 0L,
)
