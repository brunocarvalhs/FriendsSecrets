package br.com.brunocarvalhs.auth.app.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.domain.useCases.CreateProfileUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.DeleteAccountUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GetLikesProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val createProfileUseCase: CreateProfileUseCase,
    private val getLikesProfileUseCase: GetLikesProfileUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState.Idle())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: ProfileIntent) = when (intent) {
        is ProfileIntent.FetchData -> fetchLikes()
        is ProfileIntent.SaveProfile -> saveProfile(
            name = intent.name,
            photoUrl = intent.photoUrl,
            likes = intent.likes
        )
        ProfileIntent.DeleteAccount -> deleteAccount()
    }

    private fun saveProfile(name: String, photoUrl: String, likes: List<String>) {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            createProfileUseCase.invoke(name, photoUrl, likes).onSuccess {
                _uiState.value = ProfileUiState.Success
            }.onFailure {
                _uiState.value = ProfileUiState.Error(it.message ?: "Unknown error")
            }
        }
    }

    private fun fetchLikes() {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            getLikesProfileUseCase.invoke().onSuccess { user ->
                _uiState.value = ProfileUiState.Idle(
                    name = user.name,
                    photoUrl = user.photoUrl,
                    phoneNumber = user.phoneNumber,
                    likes = user.likes,
                )
            }.onFailure {
                _uiState.value = ProfileUiState.Idle()
            }
        }
    }

    private fun deleteAccount() {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            deleteAccountUseCase.invoke().onSuccess {
                _uiState.value = ProfileUiState.Success
            }.onFailure {
                _uiState.value = ProfileUiState.Error(it.message ?: "Unknown error")
            }
        }
    }
}