package br.com.brunocarvalhs.auth.app.create_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.data.model.create
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
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
class CreateProfileViewModel @Inject constructor(
    private val createProfileUseCase: CreateProfileUseCase,
    private val getLikesProfileUseCase: GetLikesProfileUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<CreateProfileUiState> = MutableStateFlow(CreateProfileUiState.Idle())
    val uiState: StateFlow<CreateProfileUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: CreateProfileIntent) = when (intent) {
        is CreateProfileIntent.FetchData -> fetchLikes()
        is CreateProfileIntent.SaveCreateProfile -> saveProfile(
            name = intent.name,
            photoUrl = intent.photoUrl,
            likes = intent.likes
        )
        CreateProfileIntent.DeleteAccount -> deleteAccount()
    }

    private fun saveProfile(name: String, photoUrl: String, likes: List<String>) {
        _uiState.value = CreateProfileUiState.Loading
        viewModelScope.launch {
            val user = UserEntities.create(name = name, photoUrl = photoUrl, likes = likes)
            createProfileUseCase.invoke(user).onSuccess {
                _uiState.value = CreateProfileUiState.Success
            }.onFailure {
                _uiState.value = CreateProfileUiState.Error(it.message ?: "Unknown error")
            }
        }
    }

    private fun fetchLikes() {
        _uiState.value = CreateProfileUiState.Loading
        viewModelScope.launch {
            getLikesProfileUseCase.invoke().onSuccess { user ->
                _uiState.value = CreateProfileUiState.Idle(
                    name = user.name,
                    photoUrl = user.photoUrl,
                    phoneNumber = user.phoneNumber,
                    likes = user.likes,
                    isAnonymous = user.isAnonymous
                )
            }.onFailure {
                _uiState.value = CreateProfileUiState.Idle()
            }
        }
    }

    private fun deleteAccount() {
        _uiState.value = CreateProfileUiState.Loading
        viewModelScope.launch {
            deleteAccountUseCase.invoke().onSuccess {
                _uiState.value = CreateProfileUiState.Success
            }.onFailure {
                _uiState.value = CreateProfileUiState.Error(it.message ?: "Unknown error")
            }
        }
    }
}
