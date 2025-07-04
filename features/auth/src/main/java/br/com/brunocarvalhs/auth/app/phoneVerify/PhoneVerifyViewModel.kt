package br.com.brunocarvalhs.auth.app.phoneVerify

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.domain.useCases.SendPhoneUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.VerifyPhoneUseCase
import com.google.firebase.perf.metrics.AddTrace
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

    @AddTrace(name = "PhoneVerifyViewModel.handleIntent", enabled = true)
    fun handleIntent(intent: PhoneVerifyIntent) = when (intent) {
        is PhoneVerifyIntent.VerifyCode -> verifyCode(intent.code)
        is PhoneVerifyIntent.ResendCode -> resendCode(
            activity = intent.activity,
            phone = intent.phone,
            countryCode = intent.countryCode
        )
    }

    @AddTrace(name = "PhoneVerifyViewModel.resetUiState", enabled = true)
    fun resetUiState() {
        _uiState.value = PhoneVerifyUiState.Idle
    }

    @AddTrace(name = "PhoneVerifyViewModel.verifyCode", enabled = true)
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

    @AddTrace(name = "PhoneVerifyViewModel.resendCode", enabled = true)
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