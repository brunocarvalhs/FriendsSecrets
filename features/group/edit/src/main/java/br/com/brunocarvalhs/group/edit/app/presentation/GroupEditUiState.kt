package br.com.brunocarvalhs.group.edit.app.presentation

import androidx.compose.runtime.Stable
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel

@Stable
data class GroupEditUiState(
    val group: GroupModel,
    val isLoading: Boolean = false,
    val error: String? = null
)
