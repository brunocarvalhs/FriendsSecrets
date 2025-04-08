package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile

sealed class ProfileUiState {
    data class Idle(val name: String? = null, val photoUrl: String? = null) : ProfileUiState()
    data object Loading : ProfileUiState()
    data object Success : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}