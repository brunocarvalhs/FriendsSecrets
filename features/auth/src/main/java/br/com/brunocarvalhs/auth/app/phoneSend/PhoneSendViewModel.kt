package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneSend

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.domain.useCases.SendPhoneUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhoneSendViewModel(
    private val sendPhoneUseCase: SendPhoneUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<PhoneSendUiState> = MutableStateFlow(PhoneSendUiState.Idle)
    val uiState: StateFlow<PhoneSendUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: PhoneSendIntent) = when (intent) {
        is PhoneSendIntent.SendCode -> sendCode(intent.phone, intent.countryCode)
    }

    private fun sendCode(phone: String, countryCode: String) {
        _uiState.value = PhoneSendUiState.Loading
        viewModelScope.launch {
            sendPhoneUseCase.invoke(phone = phone, countryCode = countryCode).onSuccess {
                _uiState.value = PhoneSendUiState.Success(phone = "$countryCode$phone")
            }.onFailure {
                _uiState.value = PhoneSendUiState.Error(it.message.orEmpty())
            }
        }

    }

    companion object {
        fun Factory(activity: ComponentActivity? = null): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    activity ?: throw IllegalArgumentException("Activity cannot be null")
                    val sendPhoneUseCase = SendPhoneUseCase(activity = activity)
                    PhoneSendViewModel(sendPhoneUseCase = sendPhoneUseCase)
                }
            }
    }
}