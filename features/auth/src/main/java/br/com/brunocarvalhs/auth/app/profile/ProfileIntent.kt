package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile

sealed class ProfileIntent {
    data object FetchData : ProfileIntent()
    data class SaveProfile(
        val name: String,
        val photoUrl: String,
        val likes: List<String>
    ) : ProfileIntent()

    data object DeleteAccount : ProfileIntent()
}