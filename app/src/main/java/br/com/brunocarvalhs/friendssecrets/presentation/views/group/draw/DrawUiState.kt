package br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw

interface DrawUiState {
    data object Idle : DrawUiState
    data object Loading : DrawUiState
    data class Success(val draw: String) : DrawUiState
    data class Error(val error: String) : DrawUiState
}