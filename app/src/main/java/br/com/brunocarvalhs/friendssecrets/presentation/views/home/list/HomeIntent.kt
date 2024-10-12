package br.com.brunocarvalhs.friendssecrets.presentation.views.home.list

sealed interface HomeIntent {
    data object FetchGroups : HomeIntent
    data class GroupToEnter(val token: String): HomeIntent
}