package br.com.brunocarvalhs.group.create.app.presentation.editForm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import br.com.brunocarvalhs.group.create.commons.components.FormsContent

@Composable
internal fun EditFormsScreen(
    viewModel: EditFormsViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    FormsContent(
        isEditing = true,
        name = uiState.name,
        description = uiState.description,
        date = uiState.date,
        minPrice = uiState.minPrice,
        maxPrice = uiState.maxPrice,
        selectedPhoto = uiState.selectedPhoto,
        availablePhotos = uiState.availablePhotos,
        onNameChange = { viewModel.handleIntent(EditFormsIntent.UpdateName(it)) },
        onDescriptionChange = { viewModel.handleIntent(EditFormsIntent.UpdateDescription(it)) },
        onDateChange = { viewModel.handleIntent(EditFormsIntent.UpdateDate(it)) },
        onMinPriceChange = { viewModel.handleIntent(EditFormsIntent.UpdateMinPrice(it)) },
        onMaxPriceChange = { viewModel.handleIntent(EditFormsIntent.UpdateMaxPrice(it)) },
        onPhotoChange = { viewModel.handleIntent(EditFormsIntent.UpdatePhoto(it)) },
        members = uiState.members,
        contacts = uiState.contacts,
        isLoading = uiState.isLoading,
        error = uiState.error,
        isValid = uiState.isValid,
        isPriceError = uiState.isPriceError,
        onBack = onBack,
        onCreate = { viewModel.handleIntent(EditFormsIntent.SaveGroup(onBack)) },
    )
}
