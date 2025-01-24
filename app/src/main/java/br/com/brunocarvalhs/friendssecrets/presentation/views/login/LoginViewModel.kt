package br.com.brunocarvalhs.friendssecrets.presentation.views.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.data.repository.UserRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.service.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.useCases.LoginUserUseCase
import com.firebase.ui.auth.AuthUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val useCase: LoginUserUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> =
        MutableStateFlow(LoginUiState.Idle)

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun onEvent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.GoogleAuth -> loginGoogle(intent.launcher)
            is LoginIntent.PhoneAuth -> loginPhone(intent.launcher)
            is LoginIntent.EmailAuth -> loginEmail(intent.launcher)
            is LoginIntent.Error -> errorMessage(intent.message)
            is LoginIntent.Success -> redirect(intent.event)
            is LoginIntent.FacebookAuth -> loginFacebook(intent.launcher)
        }
    }

    private fun errorMessage(message: String) {
        _uiState.value = LoginUiState.Error(message)
    }

    private fun redirect(event: () -> Unit) {
        viewModelScope.launch {
            useCase.invoke()
                .onSuccess {
                    _uiState.value = LoginUiState.Success
                }.onFailure {
                    errorMessage(it.message.orEmpty())
                }
        }
    }

    private fun loginFacebook(launcher: ActivityResultLauncher<Intent>) {
        val providers = arrayListOf(AuthUI.IdpConfig.FacebookBuilder().build())
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        launcher.launch(signInIntent)
    }

    private fun loginGoogle(launcher: ActivityResultLauncher<Intent>) {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        launcher.launch(signInIntent)
    }

    private fun loginPhone(launcher: ActivityResultLauncher<Intent>) {
        val providers = arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        launcher.launch(signInIntent)
    }

    private fun loginEmail(launcher: ActivityResultLauncher<Intent>) {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        launcher.launch(signInIntent)
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val useCase = LoginUserUseCase(
                        service = SessionManager(),
                        repository = UserRepositoryImpl()
                    )
                    LoginViewModel(useCase = useCase)
                }
            }
    }
}
