package br.com.brunocarvalhs.auth.app.login

internal sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object PrivacyPolicy : LoginUiState()
    data object TermsOfUse : LoginUiState()
    data object Register : LoginUiState()
    data object AcceptNotRegister : LoginUiState()
}