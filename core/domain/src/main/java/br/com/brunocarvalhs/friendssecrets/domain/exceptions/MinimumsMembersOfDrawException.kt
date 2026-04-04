package br.com.brunocarvalhs.group.core.domain.exceptions

data class MinimumsMembersOfDrawException(
    override val message: String = "Minimums members of draw"
) : Exception(message)

