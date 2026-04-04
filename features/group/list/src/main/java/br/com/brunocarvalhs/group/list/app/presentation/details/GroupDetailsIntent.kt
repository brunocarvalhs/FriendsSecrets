package br.com.brunocarvalhs.group.app.presentation.details

import android.content.Context
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.list.app.domain.entities.UserModel

sealed interface GroupDetailsIntent {
    data class FetchGroup(val groupId: String) : GroupDetailsIntent
    data class DrawMembers(val group: GroupModel) : GroupDetailsIntent
    data class ExitGroup(val groupId: String) : GroupDetailsIntent
    data class DeleteGroup(val groupId: String) : GroupDetailsIntent
    data class ShareMember(
        val context: Context,
        val member: UserModel,
        val secret: String,
        val token: String,
    ) : GroupDetailsIntent

    data class RemoveMember(
        val group: GroupModel,
        val participant: UserModel
    ) : GroupDetailsIntent

    data class EditMember(
        val group: GroupModel,
        val participant: UserModel,
    ) : GroupDetailsIntent

    data class ShareGroup(
        val context: Context,
        val group: GroupModel
    ) : GroupDetailsIntent
}
