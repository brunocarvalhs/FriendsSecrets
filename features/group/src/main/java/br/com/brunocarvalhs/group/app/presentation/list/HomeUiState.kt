package br.com.brunocarvalhs.group.app.presentation.list

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val list: List<GroupEntities>) : HomeUiState
    data class Error(val errorMessage: String) : HomeUiState
}