package br.com.brunocarvalhs.friendssecrets.presentation.views.home

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val list: List<GroupEntities>) : HomeUiState
    data class Error(val errorMessage: String) : HomeUiState
}