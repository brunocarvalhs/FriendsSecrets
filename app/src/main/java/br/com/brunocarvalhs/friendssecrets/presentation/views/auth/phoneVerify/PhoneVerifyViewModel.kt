package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneVerify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.domain.useCases.VerifyPhoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneVerifyViewModel @Inject constructor(
    private val verifyPhoneUseCase: VerifyPhoneUseCase
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
            verifyPhoneUseCase.invoke(code = code).onSuccess {
                _uiState.value = PhoneVerifyUiState.Success
            }.onFailure {
                _uiState.value = PhoneVerifyUiState.Error(it.message.orEmpty())
            }
        }
    }
}