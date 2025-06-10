package br.com.brunocarvalhs.auth.app.profiler

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

sealed class UserProfileUiState {
    data class Idle(
        val data: UserEntities? = null,
        val isAnonymous: Boolean = false
    ) : UserProfileUiState()

    data object Loading : UserProfileUiState()
    data object Success : UserProfileUiState()
    data class Error(val message: String) : UserProfileUiState()
}