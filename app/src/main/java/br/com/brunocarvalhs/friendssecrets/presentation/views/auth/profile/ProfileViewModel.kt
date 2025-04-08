package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.useCases.CreateProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val createProfileUseCase: CreateProfileUseCase,
    private val sessionManager: SessionManager = SessionManager.getInstance()
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState.Idle(
        name = sessionManager.getUserName(),
        photoUrl = sessionManager.getUserPhotoUrl()
    ))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: ProfileIntent) = when (intent) {
        is ProfileIntent.SaveProfile -> saveProfile(intent.name, intent.photoUrl)
    }

    private fun saveProfile(name: String, photoUrl: String) {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            createProfileUseCase.invoke(name, photoUrl).onSuccess {
                _uiState.value = ProfileUiState.Success
            }.onFailure {
                _uiState.value = ProfileUiState.Error(it.message ?: "Unknown error")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val createProfileUseCase = CreateProfileUseCase(
                        sessionManager = SessionManager.getInstance()
                    )
                    ProfileViewModel(createProfileUseCase = createProfileUseCase)
                }
            }
    }
}