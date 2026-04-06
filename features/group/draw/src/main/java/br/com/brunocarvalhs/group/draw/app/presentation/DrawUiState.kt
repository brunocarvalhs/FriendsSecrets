package br.com.brunocarvalhs.group.app.presentation.draw

interface DrawUiState {
    data object Idle : DrawUiState
    data object Loading : DrawUiState
    data class Success(val group: GroupEntities, val draw: Map<String, String>) : DrawUiState
    data class Error(val error: String) : DrawUiState
}