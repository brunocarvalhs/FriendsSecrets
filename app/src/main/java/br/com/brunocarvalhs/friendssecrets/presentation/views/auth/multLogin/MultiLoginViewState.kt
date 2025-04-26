package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.multLogin

data class MultiLoginViewState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
