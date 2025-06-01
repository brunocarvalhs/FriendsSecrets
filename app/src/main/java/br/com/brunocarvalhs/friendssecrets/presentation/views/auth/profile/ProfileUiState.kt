package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

sealed class ProfileUiState {
    data class Idle(val user: UserEntities? = null, ) : ProfileUiState()
    data object Loading : ProfileUiState()
    data object Success : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}