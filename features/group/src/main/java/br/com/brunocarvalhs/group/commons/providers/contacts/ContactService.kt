package br.com.brunocarvalhs.group.commons.providers.contacts

interface ContactService {
    fun getPhoneNumbers(): List<String>
    fun getContacts(): List<UserEntities>
}