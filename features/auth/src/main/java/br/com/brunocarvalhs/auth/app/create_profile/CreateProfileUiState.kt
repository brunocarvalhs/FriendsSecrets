package br.com.brunocarvalhs.auth.app.create_profile

sealed class CreateProfileUiState {
    data class Idle(
        val name: String? = null,
        val phoneNumber: String? = null,
        val photoUrl: String? = null,
        val likes: List<String> = emptyList(),
        val isAnonymous: Boolean = false
    ) : CreateProfileUiState()

    data object Loading : CreateProfileUiState()
    data object Success : CreateProfileUiState()
    data class Error(val message: String) : CreateProfileUiState()
}
