package br.com.brunocarvalhs.group.list.app.presentation

internal sealed interface GroupListIntent {
    data object FetchGroups : GroupListIntent
    data class GroupToEnter(val token: String) : GroupListIntent
    data class OnSearchQueryChange(val query: String) : GroupListIntent
    data class OnTagSelected(val tag: GroupFilterTag) : GroupListIntent
}