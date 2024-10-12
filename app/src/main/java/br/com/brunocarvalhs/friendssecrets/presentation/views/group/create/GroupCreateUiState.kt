package br.com.brunocarvalhs.friendssecrets.presentation.views.group.create

interface GroupCreateUiState {
    data object Idle : GroupCreateUiState
    data object Loading : GroupCreateUiState
    data object Success : GroupCreateUiState
    data class Error(val message: String) : GroupCreateUiState
}