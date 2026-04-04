package br.com.brunocarvalhs.group.app.presentation.edit

interface GroupEditUiState {
    data class Idle(val group: GroupEntities) : GroupEditUiState
    data object Loading : GroupEditUiState
    data object Success : GroupEditUiState
    data class Error(val message: String) : GroupEditUiState
}