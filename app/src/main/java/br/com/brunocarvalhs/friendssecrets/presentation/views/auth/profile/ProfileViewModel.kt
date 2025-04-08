package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.data.repository.UserRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.useCases.CreateProfileUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GetLikesProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val createProfileUseCase: CreateProfileUseCase,
    private val getLikesProfileUseCase: GetLikesProfileUseCase,
    sessionManager: SessionManager = SessionManager.getInstance()
) : ViewModel() {

    init {
        fetchLikes(user = sessionManager.getCurrentUserModel())
    }

    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(
        ProfileUiState.Idle(
            name = sessionManager.getUserName(),
            photoUrl = sessionManager.getUserPhotoUrl(),
            phoneNumber = sessionManager.getUserPhoneNumber(),
            likes = emptyList()
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: ProfileIntent) = when (intent) {
        is ProfileIntent.SaveProfile -> saveProfile(intent.name, intent.photoUrl)
        ProfileIntent.DeleteAccount -> deleteAccount()
        ProfileIntent.DownloadData -> downloadData()
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

    private fun fetchLikes(user: UserEntities?) {
        viewModelScope.launch {
            getLikesProfileUseCase.invoke().onSuccess { likes ->
                _uiState.value = ProfileUiState.Idle(
                    name = user?.name,
                    photoUrl = user?.photoUrl,
                    phoneNumber = user?.phoneNumber,
                    likes = likes
                )
            }
        }
    }

    private fun deleteAccount() {
        // Implementar lógica para deletar conta
    }

    private fun downloadData() {
        // Implementar lógica para baixar dados
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val sessionManager = SessionManager.getInstance()
                    val userRepository = UserRepositoryImpl()
                    val getLikesProfileUseCase = GetLikesProfileUseCase(
                        sessionManager = sessionManager,
                        userRepository = userRepository
                    )
                    val createProfileUseCase = CreateProfileUseCase(
                        sessionManager = sessionManager,
                        userRepository = userRepository
                    )
                    ProfileViewModel(
                        sessionManager = sessionManager,
                        createProfileUseCase = createProfileUseCase,
                        getLikesProfileUseCase = getLikesProfileUseCase
                    )
                }
            }
    }
}