package br.com.brunocarvalhs.group.details.app.presentation

sealed interface GroupDetailsIntent {
    data class Delete(val callback: () -> Unit) : GroupDetailsIntent
    data object Share : GroupDetailsIntent
    data class Exit(val callback: () -> Unit) : GroupDetailsIntent
}
