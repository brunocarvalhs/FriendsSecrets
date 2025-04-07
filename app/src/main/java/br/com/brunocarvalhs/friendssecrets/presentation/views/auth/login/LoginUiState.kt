package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.login

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object PrivacyPolicy : LoginUiState()
    data object TermsOfUse : LoginUiState()
    data object Register : LoginUiState()
}