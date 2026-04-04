package br.com.brunocarvalhs.group.create.app.presentation.forms

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel
import br.com.brunocarvalhs.group.create.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.useCases.GroupCreateUseCase
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class FormsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val groupCreateUseCase: GroupCreateUseCase
) : ViewModel() {

    private val args = savedStateHandle.toRoute<FormsRouter>(FormsRouter.typeMap)
    private val _uiState = MutableStateFlow(
        FormsUiState(
            members = args.members,
            contacts = args.contacts
        )
    )
    val uiState: StateFlow<FormsUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: FormsIntent) = when (intent) {
        is FormsIntent.CreateGroup -> createGroup(intent.onFinish)
        is FormsIntent.UpdateName -> _uiState.value = _uiState.value.copy(name = intent.name)
        is FormsIntent.UpdateDescription -> _uiState.value = _uiState.value.copy(description = intent.description)
        is FormsIntent.UpdateDate -> _uiState.value = _uiState.value.copy(date = intent.date)
        is FormsIntent.UpdateMinPrice -> _uiState.value = _uiState.value.copy(minPrice = intent.minPrice)
        is FormsIntent.UpdateMaxPrice -> _uiState.value = _uiState.value.copy(maxPrice = intent.maxPrice)
    }

    private fun createGroup(onFinish: (String) -> Unit) {
        viewModelScope.launch {
            val currentState = _uiState.value
            _uiState.value = currentState.copy(isLoading = true, error = null)
            
            val group = GroupModel(
                id = currentState.id,
                name = currentState.name,
                description = currentState.description,
                members = currentState.members,
                token = currentState.token,
                minPrice = currentState.minPrice.toIntOrNull(),
                maxPrice = currentState.maxPrice.toIntOrNull(),
                date = currentState.date.ifBlank { null }
            )
            
            groupCreateUseCase(group).onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false)
                onFinish(group.token)
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }
}
