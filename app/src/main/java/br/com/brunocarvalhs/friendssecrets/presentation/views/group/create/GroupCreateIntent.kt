package br.com.brunocarvalhs.friendssecrets.presentation.views.group.create

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

sealed interface GroupCreateIntent {
    data class CreateGroup(
        val name: String,
        val description: String,
        val members: List<UserEntities>
    ) : GroupCreateIntent

    data class FetchContacts(val context: Context) : GroupCreateIntent
    data object Back : GroupCreateIntent
}