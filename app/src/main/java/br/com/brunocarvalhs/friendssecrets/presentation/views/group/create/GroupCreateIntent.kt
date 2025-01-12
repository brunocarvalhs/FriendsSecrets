package br.com.brunocarvalhs.friendssecrets.presentation.views.group.create

sealed interface GroupCreateIntent {
    data class CreateGroup(val name: String, val description: String, val members: Map<String, String>) : GroupCreateIntent
    object Back : GroupCreateIntent
}