package br.com.brunocarvalhs.auth.app.splash

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

sealed class SplashUiState {
    data object Loading : SplashUiState()
    data class Success(val user: UserEntities? = null) : SplashUiState()
    data object NoSession : SplashUiState()
    data class Error(val message: String) : SplashUiState()
}