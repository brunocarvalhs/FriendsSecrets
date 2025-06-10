package br.com.brunocarvalhs.group.app.create

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

sealed interface GroupCreateIntent {
    data object FetchContacts : GroupCreateIntent
    data object CreateGroup : GroupCreateIntent
    data object NextStep : GroupCreateIntent
    data object Back : GroupCreateIntent

    data class UpdateName(val name: String) : GroupCreateIntent
    data class UpdateDescription(val description: String) : GroupCreateIntent
    data class ToggleMember(val member: UserEntities) : GroupCreateIntent
    data class UpdateDrawDate(val date: String) : GroupCreateIntent
    data class UpdateMinValue(val value: String) : GroupCreateIntent
    data class UpdateMaxValue(val value: String) : GroupCreateIntent
    data class UpdateDrawType(val type: String) : GroupCreateIntent
}
