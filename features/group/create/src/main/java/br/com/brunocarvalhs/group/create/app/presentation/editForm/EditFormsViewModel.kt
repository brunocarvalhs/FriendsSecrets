package br.com.brunocarvalhs.group.create.app.presentation.editForm

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.group.create.app.domain.services.GroupImageService
import br.com.brunocarvalhs.group.create.app.domain.useCases.GroupEditUseCase
import br.com.brunocarvalhs.group.create.commons.analytics.GroupCreateAnalytics
import br.com.brunocarvalhs.group.create.commons.navigation.EditFormsRouter
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
internal class EditFormsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val groupEditUseCase: GroupEditUseCase,
    private val imageService: GroupImageService,
    private val analytics: GroupCreateAnalytics
) : ViewModel() {

    private val args = savedStateHandle.toRoute<EditFormsRouter>(EditFormsRouter.typeMap)
    private val _uiState = MutableStateFlow(
        EditFormsUiState(
            name = args.group.name,
            description = args.group.description.orEmpty(),
            date = args.group.date.orEmpty(),
            minPrice = args.group.minPrice.toString(),
            maxPrice = args.group.maxPrice.toString(),
            members = args.group.members,
            selectedPhoto = args.group.photo,
        )
    )
    val uiState: StateFlow<EditFormsUiState> = _uiState.asStateFlow()

    init {
        analytics.trackEditFormScreenView()
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

    @AddTrace(name = "EditFormsViewModel.handleIntent", enabled = true)
    fun handleIntent(intent: EditFormsIntent) = when (intent) {
        is EditFormsIntent.SaveGroup -> saveGroup(intent.onFinish)
        is EditFormsIntent.UpdateName -> updateState { copy(name = intent.name) }
        is EditFormsIntent.UpdateDescription -> updateState { copy(description = intent.description) }
        is EditFormsIntent.UpdateDate -> updateState { copy(date = intent.date) }
        is EditFormsIntent.UpdateMinPrice -> updateState { copy(minPrice = intent.minPrice) }
        is EditFormsIntent.UpdateMaxPrice -> updateState { copy(maxPrice = intent.maxPrice) }
        is EditFormsIntent.UpdatePhoto -> updateState { copy(selectedPhoto = intent.photoUrl) }
    }

    private fun updateState(update: EditFormsUiState.() -> EditFormsUiState) {
        _uiState.update { 
            val newState = it.update()
            validate(newState)
        }
    }

    private fun validate(state: EditFormsUiState): EditFormsUiState {
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

    @AddTrace(name = "EditFormsViewModel.saveGroup", enabled = true)
    private fun saveGroup(onFinish: () -> Unit) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (!currentState.isValid) return@launch
            
            _uiState.value = currentState.copy(isLoading = true, error = null)

            val group = args.group.copy(
                id = args.group.id,
                name = currentState.name,
                description = currentState.description,
                date = currentState.date,
                minPrice = currentState.minPrice.toDoubleOrNull(),
                maxPrice = currentState.maxPrice.toDoubleOrNull(),
                photo = currentState.selectedPhoto,
                members = currentState.members,
                type = args.group.type,
            )

            groupEditUseCase(group).onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false)
                onFinish()
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }
}
