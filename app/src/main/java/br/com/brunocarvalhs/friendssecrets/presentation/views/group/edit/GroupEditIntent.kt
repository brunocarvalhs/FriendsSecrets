package br.com.brunocarvalhs.friendssecrets.presentation.views.group.edit

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities

sealed interface GroupEditIntent {
    data class FetchGroup(val id: String): GroupEditIntent
    data class EditGroup(val group: GroupEntities) : GroupEditIntent
}