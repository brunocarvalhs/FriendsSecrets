package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.multLogin

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.domain.useCases.LoginUserUseCase
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiLoginViewModel @Inject constructor(
    private val useCase: LoginUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MultiLoginViewState())
    val state: StateFlow<MultiLoginViewState> = _state

    fun handlerIntent(intent: MultiLoginViewIntent) {
        when (intent) {
            is MultiLoginViewIntent.GoogleAuth -> loginGoogle(intent.launcher)
            is MultiLoginViewIntent.PhoneAuth -> loginPhone(intent.launcher)
            is MultiLoginViewIntent.EmailAuth -> loginEmail(intent.launcher)
            is MultiLoginViewIntent.Error -> errorMessage(intent.message)
            is MultiLoginViewIntent.Success -> redirect(intent.event)
        }
    }

    private fun errorMessage(message: String) {
        _state.value = _state.value.copy(error = message, isLoading = false)
    }

    private fun redirect(event: () -> Unit) {
        viewModelScope.launch {
            useCase.invoke().onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    success = true
                )
                event.invoke()
            }.onFailure { errorMessage(it.message.orEmpty()) }
        }
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
}
