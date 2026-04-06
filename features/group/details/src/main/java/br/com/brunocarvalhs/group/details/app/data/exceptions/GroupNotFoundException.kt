package br.com.brunocarvalhs.group.details.app.data.exceptions

data class GroupNotFoundException(
    override val message: String = "Group not found"
) : Exception(message)
