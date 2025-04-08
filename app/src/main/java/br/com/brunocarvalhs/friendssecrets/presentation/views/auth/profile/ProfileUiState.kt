package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile

sealed class ProfileUiState {
    data object Idle : ProfileUiState()
    data object Loading : ProfileUiState()
    data object Success : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}