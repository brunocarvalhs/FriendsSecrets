package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile

sealed class ProfileIntent {
    data class SaveProfile(
        val name: String,
        val photoUrl: String
    ) : ProfileIntent()
}