package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities

interface GroupDetailsUiState {
    data object Loading : GroupDetailsUiState
    data object Draw : GroupDetailsUiState
    data object Exit : GroupDetailsUiState
    data class Success(val group: GroupEntities) : GroupDetailsUiState
    data class Error(val message: String) : GroupDetailsUiState
}