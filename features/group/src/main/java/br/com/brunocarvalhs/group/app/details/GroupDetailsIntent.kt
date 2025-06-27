package br.com.brunocarvalhs.group.app.details

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

sealed interface GroupDetailsIntent {
    data class FetchGroup(val groupId: String) : GroupDetailsIntent
    data class DrawMembers(val group: GroupEntities) : GroupDetailsIntent
    data class ExitGroup(val groupId: String) : GroupDetailsIntent
    data class DeleteGroup(val groupId: String) : GroupDetailsIntent
    data class ShareMember(
        val context: Context,
        val member: UserEntities,
        val secret: String,
        val token: String,
    ) : GroupDetailsIntent
    data class RemoveMember(
        val group: GroupEntities,
        val participant: UserEntities
    ): GroupDetailsIntent
    data class EditMember(
        val group: GroupEntities,
        val participant: UserEntities,
    ): GroupDetailsIntent
    data class ShareGroup(
        val context: Context,
        val group: GroupEntities
    ): GroupDetailsIntent
}
