package br.com.brunocarvalhs.group.details.app.presentation

internal sealed interface GroupDetailsIntent {
    data object Refresh : GroupDetailsIntent
    data class Delete(val callback: () -> Unit) : GroupDetailsIntent
    data object Share : GroupDetailsIntent
    data class Exit(val callback: () -> Unit) : GroupDetailsIntent
    data class UpdateLikes(val likes: List<String>) : GroupDetailsIntent
}
