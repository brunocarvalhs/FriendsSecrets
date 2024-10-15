package br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities

interface DrawUiState {
    data object Idle : DrawUiState
    data object Loading : DrawUiState
    data class Success(val group: GroupEntities, val draw: Map<String, String>) : DrawUiState
    data class Error(val error: String) : DrawUiState
}