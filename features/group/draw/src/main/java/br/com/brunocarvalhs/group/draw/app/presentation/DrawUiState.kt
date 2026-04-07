package br.com.brunocarvalhs.group.draw.app.presentation

import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel

data class DrawUiState(
    val isLoading: Boolean = false,
    val members: List<UserModel> = emptyList(),
    val error: String? = null,
    val draw: Map<String, String> = emptyMap(),
    val isDraw: Boolean = false,
)