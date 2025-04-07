package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

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
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    LoginViewModel()
                }
            }
    }
}