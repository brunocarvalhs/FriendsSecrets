package br.com.brunocarvalhs.group.details.app.presentation

import br.com.brunocarvalhs.core.domain.model.GroupModel

internal data class GroupDetailsUiState(
    val group: GroupModel,
    val isReminderEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
