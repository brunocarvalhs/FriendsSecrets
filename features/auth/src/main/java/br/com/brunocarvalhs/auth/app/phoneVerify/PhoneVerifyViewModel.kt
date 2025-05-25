package br.com.brunocarvalhs.auth.app.phoneVerify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.domain.useCases.VerifyPhoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneVerifyViewModel @Inject constructor(
    private val useCase: VerifyPhoneUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<PhoneVerifyUiState> =
        MutableStateFlow(PhoneVerifyUiState.Idle)
    val uiState: StateFlow<PhoneVerifyUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: PhoneVerifyIntent) = when (intent) {
        is PhoneVerifyIntent.VerifyCode -> verifyCode(intent.code)
    }

    private fun verifyCode(code: String) {
        _uiState.value = PhoneVerifyUiState.Loading
        viewModelScope.launch {
            useCase.invoke(code = code).onSuccess {
                _uiState.value = PhoneVerifyUiState.Success
            }.onFailure {
                _uiState.value = PhoneVerifyUiState.Error(it.message.orEmpty())
            }
        }
    }
}