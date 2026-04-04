package br.com.brunocarvalhs.group.app.presentation.details

interface GroupDetailsUiState {
    data object Loading : GroupDetailsUiState
    data object Draw : GroupDetailsUiState
    data object Exit : GroupDetailsUiState
    data class Success(val group: GroupEntities) : GroupDetailsUiState
    data class Error(val message: String) : GroupDetailsUiState
    data class EditMember(
        val group: GroupEntities,
        val participant: String,
        val likes: List<String>
    ) : GroupDetailsUiState
}