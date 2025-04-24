package br.com.brunocarvalhs.friendssecrets.presentation.views.group.create

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

interface GroupCreateUiState {
    data class Idle(
        val contacts: List<UserEntities> = emptyList(),
    ) : GroupCreateUiState
    data object Loading : GroupCreateUiState
    data object Success : GroupCreateUiState
    data class Error(val message: String) : GroupCreateUiState
}