package br.com.brunocarvalhs.friendssecrets.domain.services

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

interface ContactService {
    fun getPhoneNumbers(): List<String>
    fun getContacts(): List<UserEntities>
}