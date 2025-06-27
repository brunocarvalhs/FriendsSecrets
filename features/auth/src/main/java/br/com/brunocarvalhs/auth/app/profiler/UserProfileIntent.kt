package br.com.brunocarvalhs.auth.app.profiler

sealed class UserProfileIntent {
    data object FetchData : UserProfileIntent()

    data class SaveProfile(
        val name: String,
        val photoUrl: String,
        val likes: List<String>
    ) : UserProfileIntent()

    data object DeleteAccount : UserProfileIntent()
    data object Logout : UserProfileIntent()
}