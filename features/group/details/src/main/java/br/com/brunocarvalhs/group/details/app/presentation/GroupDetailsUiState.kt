package br.com.brunocarvalhs.group.details.app.presentation

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.domain.model.GiftSuggestion

internal data class GroupDetailsUiState(
    val group: GroupModel,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuggestingGifts: Boolean = false,
    val giftSuggestions: List<GiftSuggestion>? = null,
)
