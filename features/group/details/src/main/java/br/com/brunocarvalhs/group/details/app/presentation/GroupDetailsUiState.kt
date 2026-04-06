package br.com.brunocarvalhs.group.details.app.presentation

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel

data class GroupDetailsUiState(
    val group: GroupModel,
    val isLoading: Boolean = false,
    val error: String? = null,
)
