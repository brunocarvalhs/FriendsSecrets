package br.com.brunocarvalhs.auth.app.login

import androidx.lifecycle.ViewModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    val session: SessionService<UserEntities>
) : ViewModel() {
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
            session.setUserAnonymous()
            _uiState.value = LoginUiState.AcceptNotRegister
        }
    }
}