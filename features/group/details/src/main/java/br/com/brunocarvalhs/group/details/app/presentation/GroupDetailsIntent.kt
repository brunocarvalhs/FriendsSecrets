package br.com.brunocarvalhs.group.details.app.presentation

internal sealed interface GroupDetailsIntent {
    data object Refresh : GroupDetailsIntent
    data class Delete(val callback: () -> Unit) : GroupDetailsIntent
    data object Share : GroupDetailsIntent
    data object ShareWishlist : GroupDetailsIntent
    data class Exit(val callback: () -> Unit) : GroupDetailsIntent
    data class ToggleReminder(val enabled: Boolean) : GroupDetailsIntent
    data class RemoveMember(val memberId: String) : GroupDetailsIntent
    data class UpdateLikes(val likes: List<String>) : GroupDetailsIntent
}
