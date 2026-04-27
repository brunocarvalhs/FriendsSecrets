package br.com.brunocarvalhs.group.create.app.presentation.forms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import br.com.brunocarvalhs.group.create.commons.components.FormsContent

@Composable
internal fun FormsScreen(
    viewModel: FormsViewModel,
    onFinish: (String) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    FormsContent(
        name = uiState.name,
        description = uiState.description,
        date = uiState.date,
        minPrice = uiState.minPrice,
        maxPrice = uiState.maxPrice,
        selectedPhoto = uiState.selectedPhoto,
        availablePhotos = uiState.availablePhotos,
        onNameChange = { viewModel.handleIntent(FormsIntent.UpdateName(it)) },
        onDescriptionChange = { viewModel.handleIntent(FormsIntent.UpdateDescription(it)) },
        onDateChange = { viewModel.handleIntent(FormsIntent.UpdateDate(it)) },
        onMinPriceChange = { viewModel.handleIntent(FormsIntent.UpdateMinPrice(it)) },
        onMaxPriceChange = { viewModel.handleIntent(FormsIntent.UpdateMaxPrice(it)) },
        onPhotoChange = { viewModel.handleIntent(FormsIntent.UpdatePhoto(it)) },
        members = uiState.members,
        isLoading = uiState.isLoading,
        isValid = uiState.isValid,
        isPriceError = uiState.isPriceError,
        onBack = onBack,
        onCreate = { viewModel.handleIntent(FormsIntent.CreateGroup(onFinish)) },
    )
}
