package br.com.brunocarvalhs.auth.app.create_profile

sealed class CreateProfileIntent {
    data object FetchData : CreateProfileIntent()
    data class SaveCreateProfile(
        val name: String,
        val photoUrl: String,
        val likes: List<String>
    ) : CreateProfileIntent()

    data object DeleteAccount : CreateProfileIntent()
}