package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities

sealed interface GroupDetailsIntent {
    data class FetchGroup(val groupId: String) : GroupDetailsIntent
    data class DrawMembers(val group: GroupEntities) : GroupDetailsIntent
    data class ExitGroup(val groupId: String) : GroupDetailsIntent
    data class DeleteGroup(val groupId: String) : GroupDetailsIntent
    data class ShareMember(
        val context: Context,
        val member: String,
        val secret: String,
        val token: String,
    ) : GroupDetailsIntent
    data class RemoveMember(
        val group: GroupEntities,
        val participant: String
    ): GroupDetailsIntent
    data class EditMember(
        val group: GroupEntities,
        val participant: String,
        val likes: List<String>
    ): GroupDetailsIntent
    data class ShareGroup(
        val context: Context,
        val group: GroupEntities
    ): GroupDetailsIntent
}
