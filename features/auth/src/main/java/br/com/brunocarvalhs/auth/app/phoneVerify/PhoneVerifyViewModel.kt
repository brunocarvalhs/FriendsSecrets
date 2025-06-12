package br.com.brunocarvalhs.auth.app.phoneVerify

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.domain.useCases.SendPhoneUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.VerifyPhoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneVerifyViewModel @Inject constructor(
    private val verifyPhoneUseCase: VerifyPhoneUseCase,
    private val sendPhoneUseCase: SendPhoneUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<PhoneVerifyUiState> =
        MutableStateFlow(PhoneVerifyUiState.Idle)
    val uiState: StateFlow<PhoneVerifyUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: PhoneVerifyIntent) = when (intent) {
        is PhoneVerifyIntent.VerifyCode -> verifyCode(intent.code)
        is PhoneVerifyIntent.ResendCode -> resendCode(
            activity = intent.activity,
            phone = intent.phone,
            countryCode = intent.countryCode
        )
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

    private fun resendCode(activity: Activity, phone: String, countryCode: String) {
        _uiState.value = PhoneVerifyUiState.Loading
        viewModelScope.launch {
            sendPhoneUseCase.invoke(phone = phone, countryCode = countryCode, activity = activity)
                .onSuccess {
                    _uiState.value = PhoneVerifyUiState.Success
                }.onFailure {
                    _uiState.value = PhoneVerifyUiState.Error(it.message.orEmpty())
                }
        }
    }
}