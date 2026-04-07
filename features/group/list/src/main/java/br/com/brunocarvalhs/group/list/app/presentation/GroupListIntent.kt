package br.com.brunocarvalhs.group.list.app.presentation

sealed interface GroupListIntent {
    data object FetchGroups : GroupListIntent
    data class GroupToEnter(val token: String) : GroupListIntent
    data class OnSearchQueryChange(val query: String) : GroupListIntent
    data class OnTagSelected(val tag: String) : GroupListIntent
}