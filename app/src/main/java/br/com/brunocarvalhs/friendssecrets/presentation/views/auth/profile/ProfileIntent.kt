package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

sealed class ProfileIntent {
    data object FetchData : ProfileIntent()
    data class SaveProfile(val user: UserEntities) : ProfileIntent()

    data object DeleteAccount : ProfileIntent()
}