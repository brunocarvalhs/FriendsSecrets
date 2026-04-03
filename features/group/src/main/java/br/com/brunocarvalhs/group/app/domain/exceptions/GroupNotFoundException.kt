package br.com.brunocarvalhs.group.app.domain.exceptions

data class GroupNotFoundException(
    override val message: String = "Group not found"
) : Exception(message)