package br.com.brunocarvalhs.group.create.app.presentation.editForm

import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel

data class EditFormsUiState(
    val name: String = "",
    val description: String = "",
    val date: String = "",
    val minPrice: String = "",
    val maxPrice: String = "",
    val members: List<UserModel> = emptyList(),
    val contacts: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPriceError: Boolean = false,
    val isValid: Boolean = false,
    val availablePhotos: List<String> = emptyList(),
    val selectedPhoto: String? = null
)