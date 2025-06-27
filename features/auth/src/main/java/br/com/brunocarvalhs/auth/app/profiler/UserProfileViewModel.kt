package br.com.brunocarvalhs.auth.app.profiler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.data.model.create
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService
import br.com.brunocarvalhs.friendssecrets.domain.useCases.CreateProfileUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.DeleteAccountUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GetLikesProfileUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val createProfileUseCase: CreateProfileUseCase,
    private val getLikesProfileUseCase: GetLikesProfileUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val session: SessionService<UserEntities>,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserProfileUiState>(UserProfileUiState.Loading)
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    fun handleIntent(intent: UserProfileIntent) = when (intent) {
        is UserProfileIntent.FetchData -> fetchData()
        is UserProfileIntent.SaveProfile -> saveProfile(intent.name, intent.photoUrl, intent.likes)
        UserProfileIntent.DeleteAccount -> deleteAccount()
        UserProfileIntent.Logout -> logout()
    }

    private fun fetchData() {
        _uiState.value = UserProfileUiState.Loading
        viewModelScope.launch {
            val isAnonymous = session.getCurrentUserModel()?.isAnonymous ?: true
            getLikesProfileUseCase.invoke().onSuccess { user ->
                _uiState.value = UserProfileUiState.Idle(data = user)
            }.onFailure {
                _uiState.value = UserProfileUiState.Idle(isAnonymous = isAnonymous)
            }
        }
    }

    private fun saveProfile(name: String, photoUrl: String, likes: List<String>) {
        _uiState.value = UserProfileUiState.Loading
        viewModelScope.launch {
            val user = UserEntities.create(name = name, photoUrl = photoUrl, likes = likes)
            createProfileUseCase(user).onSuccess {
                _uiState.value = UserProfileUiState.Success
            }.onFailure {
                _uiState.value = UserProfileUiState.Error(it.message ?: "Erro ao salvar")
            }
        }
    }

    private fun deleteAccount() {
        _uiState.value = UserProfileUiState.Loading
        viewModelScope.launch {
            deleteAccountUseCase().onSuccess {
                _uiState.value = UserProfileUiState.Success
            }.onFailure {
                _uiState.value = UserProfileUiState.Error(it.message ?: "Erro ao excluir conta")
            }
        }
    }

    private fun logout() {
        _uiState.value = UserProfileUiState.Loading
        viewModelScope.launch {
            logoutUseCase().onSuccess {
                _uiState.value = UserProfileUiState.Success
            }.onFailure {
                _uiState.value = UserProfileUiState.Error(it.message ?: "Erro ao sair")
            }
        }
    }
}
