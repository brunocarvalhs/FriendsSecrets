package br.com.brunocarvalhs.group.create.app.presentation.editForm

import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.group.create.app.domain.constants.EMPTY_STRING

internal data class EditFormsUiState(
    val name: String = EMPTY_STRING,
    val description: String = EMPTY_STRING,
    val date: String = EMPTY_STRING,
    val minPrice: String = EMPTY_STRING,
    val maxPrice: String = EMPTY_STRING,
    val members: List<UserModel> = emptyList(),
    val contacts: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPriceError: Boolean = false,
    val isValid: Boolean = false,
    val availablePhotos: List<String> = emptyList(),
    val selectedPhoto: String? = null
)
