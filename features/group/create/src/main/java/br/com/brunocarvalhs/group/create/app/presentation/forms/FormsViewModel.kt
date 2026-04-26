package br.com.brunocarvalhs.group.create.app.presentation.forms

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.services.GroupImageService
import br.com.brunocarvalhs.group.create.app.domain.useCases.GroupCreateUseCase
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
internal class FormsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val groupCreateUseCase: GroupCreateUseCase,
    private val imageService: GroupImageService,
) : ViewModel() {

    private val args = savedStateHandle.toRoute<FormsRouter>(FormsRouter.typeMap)
    private val _uiState = MutableStateFlow(
        FormsUiState(
            members = args.members,
        )
    )
    val uiState: StateFlow<FormsUiState> = _uiState.asStateFlow()

    init {
        initializer()
    }

    @AddTrace(name = "FormsViewModel.handleIntent", enabled = true)
    fun handleIntent(intent: FormsIntent) = when (intent) {
        is FormsIntent.CreateGroup -> createGroup(intent.onFinish)
        is FormsIntent.UpdateName -> updateState { copy(name = intent.name) }
        is FormsIntent.UpdateDescription -> updateState { copy(description = intent.description) }
        is FormsIntent.UpdateDate -> updateState { copy(date = intent.date) }
        is FormsIntent.UpdateMinPrice -> updateState { copy(minPrice = intent.minPrice) }
        is FormsIntent.UpdateMaxPrice -> updateState { copy(maxPrice = intent.maxPrice) }
        is FormsIntent.UpdatePhoto -> updateState { copy(selectedPhoto = intent.photoUrl) }
    }

    private fun initializer() {
        viewModelScope.launch {
            imageService.availablePhotos.collectLatest { photos ->
                _uiState.update {
                    it.copy(
                        availablePhotos = photos,
                        selectedPhoto = it.selectedPhoto ?: photos.firstOrNull()
                    )
                }
            }
        }
    }

    private fun updateState(update: FormsUiState.() -> FormsUiState) {
        _uiState.update { 
            val newState = it.update()
            validate(newState)
        }
    }

    private fun validate(state: FormsUiState): FormsUiState {
        val min = state.minPrice.toLongOrNull() ?: 0L
        val max = state.maxPrice.toLongOrNull() ?: Long.MAX_VALUE
        
        val isPriceError = state.minPrice.isNotEmpty() && state.maxPrice.isNotEmpty() && min > max
        val isNameValid = state.name.isNotBlank()
        val isMembersValid = state.members.isNotEmpty()

        return state.copy(
            isPriceError = isPriceError,
            isValid = isNameValid && isMembersValid && !isPriceError
        )
    }

    @AddTrace(name = "FormsViewModel.createGroup", enabled = true)
    private fun createGroup(onFinish: (String) -> Unit) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (!currentState.isValid) return@launch
            
            _uiState.value = currentState.copy(isLoading = true, error = null)
            
            val group = GroupModel(
                name = currentState.name,
                description = currentState.description,
                members = currentState.members,
                minPrice = currentState.minPrice.toDoubleOrNull(),
                maxPrice = currentState.maxPrice.toDoubleOrNull(),
                date = currentState.date.ifBlank { null },
                photo = currentState.selectedPhoto
            )
            
            groupCreateUseCase(group).onSuccess {
                success()
                onFinish(group.token)
            }.onFailure { error(it.message) }
        }
    }

    private fun success() {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = null
        )
    }

    private fun error(message: String?) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = message
        )
    }
}
