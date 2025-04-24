package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneVerify

sealed class PhoneVerifyUiState {
    data object Idle : PhoneVerifyUiState()
    data object Loading : PhoneVerifyUiState()
    data object Success : PhoneVerifyUiState()
    data class Error(val message: String) : PhoneVerifyUiState()
}