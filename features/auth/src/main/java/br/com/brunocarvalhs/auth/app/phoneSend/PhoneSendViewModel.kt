package br.com.brunocarvalhs.auth.app.phoneSend

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.domain.useCases.SendPhoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneSendViewModel @Inject constructor(
    private val useCase: SendPhoneUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<PhoneSendUiState> =
        MutableStateFlow(PhoneSendUiState.Idle)
    val uiState: StateFlow<PhoneSendUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: PhoneSendIntent) = when (intent) {
        is PhoneSendIntent.SendCode -> sendCode(
            activity = intent.activity,
            phone = intent.phone,
            countryCode = intent.countryCode
        )
    }

    private fun sendCode(activity: Activity, phone: String, countryCode: String) {
        _uiState.value = PhoneSendUiState.Loading
        viewModelScope.launch {
            useCase.invoke(phone = phone, countryCode = countryCode, activity = activity)
                .onSuccess {
                    _uiState.value = PhoneSendUiState.Success(phone = "$countryCode$phone")
                }.onFailure {
                    _uiState.value = PhoneSendUiState.Error(it.message.orEmpty())
                }
        }

    }
}