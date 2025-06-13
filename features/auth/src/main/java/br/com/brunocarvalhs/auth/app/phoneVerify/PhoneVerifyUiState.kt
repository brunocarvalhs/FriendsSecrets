package br.com.brunocarvalhs.auth.app.phoneVerify

sealed class PhoneVerifyUiState {
    data object Idle : PhoneVerifyUiState()
    data object Loading : PhoneVerifyUiState()
    data object Success : PhoneVerifyUiState()
    data class Error(val message: String) : PhoneVerifyUiState()
}