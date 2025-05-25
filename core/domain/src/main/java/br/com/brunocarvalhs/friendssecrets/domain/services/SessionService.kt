package br.com.brunocarvalhs.friendssecrets.domain.services

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

interface SessionService {
    fun updateUserProfile(name: String, photoUrl: String)
    fun getCurrentUserModel(): UserEntities?
    fun deleteAccount()
    fun signOut()
    fun setUserAnonymous()
}