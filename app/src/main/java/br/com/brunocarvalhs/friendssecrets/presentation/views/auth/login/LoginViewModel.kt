package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: LoginIntent) = when (intent) {
        is LoginIntent.Accept -> {
            _uiState.value = LoginUiState.Register
        }

        is LoginIntent.PrivacyPolicy -> {
            _uiState.value = LoginUiState.PrivacyPolicy
        }

        is LoginIntent.TermsOfUse -> {
            _uiState.value = LoginUiState.TermsOfUse
        }

        LoginIntent.AcceptNotRegister -> {
            _uiState.value = LoginUiState.AcceptNotRegister
        }
    }
}