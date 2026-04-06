package br.com.brunocarvalhs.group.list.app.presentation.details

sealed interface GroupDetailsIntent {
    data class Delete(val callback: () -> Unit) : GroupDetailsIntent
    data object Share : GroupDetailsIntent
    data class Exit(val callback: () -> Unit) : GroupDetailsIntent
    data object Draw : GroupDetailsIntent
    data object Reveal : GroupDetailsIntent
    data class SelectMember(val name: String) : GroupDetailsIntent
    data class ChangeCode(val code: String) : GroupDetailsIntent
    data class ConfirmReveal(val name: String) : GroupDetailsIntent
    data object DismissReveal : GroupDetailsIntent
}
