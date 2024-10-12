package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities

sealed interface GroupDetailsIntent {
    data class FetchGroup(val groupId: String) : GroupDetailsIntent
    data class DrawMembers(val group: GroupEntities) : GroupDetailsIntent
    class ShareMember(
        val context: Context,
        val member: String,
        val secret: String,
        val token: String,
    ) : GroupDetailsIntent
}
