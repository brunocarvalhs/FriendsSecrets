package br.com.brunocarvalhs.friendssecrets.domain.exceptions

data class GroupNotFoundException(
    override val message: String = "Group not found"
) : Exception(message)