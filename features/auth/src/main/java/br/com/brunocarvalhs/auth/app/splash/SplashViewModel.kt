package br.com.brunocarvalhs.auth.app.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val session: SessionService<UserEntities>,
) : ViewModel() {
    private val _state = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val state: StateFlow<SplashUiState> = _state

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            if (session.isUserLoggedIn()) {
                val data = session.getCurrentUserModel()
                _state.value = SplashUiState.Success(data)
            } else {
                _state.value = SplashUiState.NoSession
            }
        }
    }
}