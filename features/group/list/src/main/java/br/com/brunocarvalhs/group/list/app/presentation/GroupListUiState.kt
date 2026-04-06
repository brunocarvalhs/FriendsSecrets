package br.com.brunocarvalhs.group.list.app.presentation

import androidx.compose.runtime.Stable
import br.com.brunocarvalhs.group.list.app.domain.model.GroupModel

@Stable
sealed interface GroupListUiState {
    data object Loading : GroupListUiState
    data class Success(val list: List<GroupModel>) : GroupListUiState
    data class Error(val errorMessage: String) : GroupListUiState
}