package br.com.brunocarvalhs.group.list.app.presentation

sealed interface GroupListIntent {
    data object FetchGroups : GroupListIntent
    data class GroupToEnter(val token: String): GroupListIntent
}