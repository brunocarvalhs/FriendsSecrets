package br.com.brunocarvalhs.group.list.app.presentation.details

import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel

data class GroupDetailsUiState(
    val group: GroupModel,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showRevealModal: Boolean = false,
    val selectedMember: String? = null,
    val revelationCode: String = "",
    val revealedFriend: String? = null
)
