package br.com.brunocarvalhs.auth.app.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService
import br.com.brunocarvalhs.friendssecrets.domain.useCases.LoginAnonymousUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val useCase: LoginAnonymousUseCase,
    private val session: SessionService<UserEntities>,
) : ViewModel() {
    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.Logged)
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

        LoginIntent.AcceptNotRegister -> accept()
        LoginIntent.Logged -> logged()
    }

    private fun logged() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            if (session.isUserLoggedIn()) {
                _uiState.value = LoginUiState.Logged
            }
        }
    }

    private fun accept() {
        viewModelScope.launch {
            useCase.invoke().onSuccess {
                _uiState.value = LoginUiState.AcceptNotRegister
            }
        }
    }
}