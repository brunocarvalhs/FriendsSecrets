package br.com.brunocarvalhs.friendssecrets.domain.exceptions

data class MinimumsMembersOfDrawException(
    override val message: String = "Minimums members of draw"
) : Exception(message)