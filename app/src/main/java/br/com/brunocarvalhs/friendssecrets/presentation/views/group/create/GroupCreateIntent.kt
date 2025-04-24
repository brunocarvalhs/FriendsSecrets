package br.com.brunocarvalhs.friendssecrets.presentation.views.group.create

import android.content.Context

sealed interface GroupCreateIntent {
    data class CreateGroup(val name: String, val description: String, val members: Map<String, String>) : GroupCreateIntent
    data class FetchContacts(val context: Context) : GroupCreateIntent
    data object Back : GroupCreateIntent
}