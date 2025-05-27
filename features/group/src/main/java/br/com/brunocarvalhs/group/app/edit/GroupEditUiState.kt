package br.com.brunocarvalhs.group.app.edit

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities

interface GroupEditUiState {
    data class Idle(val group: GroupEntities) : GroupEditUiState
    data object Loading : GroupEditUiState
    data object Success : GroupEditUiState
    data class Error(val message: String) : GroupEditUiState
}