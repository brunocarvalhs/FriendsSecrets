package br.com.brunocarvalhs.friendssecrets.presentation.views.login

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Error(val message: String) : LoginUiState
    object Success : LoginUiState
}
