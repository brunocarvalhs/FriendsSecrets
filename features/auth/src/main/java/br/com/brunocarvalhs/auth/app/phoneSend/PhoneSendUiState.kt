package br.com.brunocarvalhs.auth.app.phoneSend

sealed class PhoneSendUiState {
    data object Idle : PhoneSendUiState()
    data class Success(val phone: String, val countryCode: String) : PhoneSendUiState()
    data object Loading : PhoneSendUiState()
    data class Error(val message: String) : PhoneSendUiState()
}