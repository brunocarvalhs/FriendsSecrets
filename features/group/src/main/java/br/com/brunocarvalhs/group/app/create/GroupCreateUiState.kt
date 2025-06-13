package br.com.brunocarvalhs.group.app.create

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

data class GroupCreateUiState(
    val name: String = "",
    val description: String = "",
    val members: List<UserEntities> = emptyList(),
    val contacts: List<UserEntities> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val currentStep: Int = 0,
    val drawDate: String = "",
    val minValue: String = "",
    val maxValue: String = "",
    val drawType: String = ""
)
