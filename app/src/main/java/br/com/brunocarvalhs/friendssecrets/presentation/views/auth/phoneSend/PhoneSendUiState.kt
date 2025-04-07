package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneSend

sealed class PhoneSendUiState {
    data object Idle : PhoneSendUiState()
    data class Success(val phone: String) : PhoneSendUiState()
    data object Loading : PhoneSendUiState()
    data class Error(val message: String) : PhoneSendUiState()
}