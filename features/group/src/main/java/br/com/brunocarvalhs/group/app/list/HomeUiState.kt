package br.com.brunocarvalhs.group.app.list

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val list: List<GroupEntities>) : HomeUiState
    data class Error(val errorMessage: String) : HomeUiState
}