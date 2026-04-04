package br.com.brunocarvalhs.group.list.app.data.exceptions

data class GroupNotFoundException(
    override val message: String = "Group not found"
) : Exception(message)
