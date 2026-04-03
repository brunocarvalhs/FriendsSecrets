package br.com.brunocarvalhs.group.app.presentation.create

data class GroupCreateUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, Int> = emptyMap(),

    val contacts: List<UserEntities> = emptyList(),
    val filteredContacts: List<UserEntities> = emptyList(),
    val members: List<UserEntities> = emptyList(),

    val name: String = "",
    val description: String = "",
    val drawDate: String = "",
    val minValue: String = "",
    val maxValue: String = "",
    val drawType: String = "",

    val searchQuery: String = "",
    val isValid: Boolean = false
)
