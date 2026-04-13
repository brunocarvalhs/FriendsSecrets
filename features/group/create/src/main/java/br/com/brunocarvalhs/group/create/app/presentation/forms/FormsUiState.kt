package br.com.brunocarvalhs.group.create.app.presentation.forms

import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel

data class FormsUiState(
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
    val availablePhotos: List<String> = listOf(
        "https://cdn-icons-png.flaticon.com/512/3751/3751473.png", // Gift (Default)
        "https://cdn-icons-png.flaticon.com/512/3751/3751475.png", // Christmas Tree
        "https://cdn-icons-png.flaticon.com/512/3751/3751470.png", // Santa
        "https://cdn-icons-png.flaticon.com/512/3751/3751467.png", // Snowman
        "https://cdn-icons-png.flaticon.com/512/3751/3751483.png", // Candy
        "https://cdn-icons-png.flaticon.com/512/3751/3751490.png"  // Ornament
    ),
    val selectedPhoto: String? = "https://cdn-icons-png.flaticon.com/512/3751/3751473.png"
)