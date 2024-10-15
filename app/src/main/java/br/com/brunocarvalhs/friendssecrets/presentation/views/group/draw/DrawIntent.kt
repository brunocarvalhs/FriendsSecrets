package br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw

sealed interface DrawIntent {
    data object Refresh : DrawIntent
    data class FetchDraw(val group: String, val code: String? = null) : DrawIntent
}
