package br.com.brunocarvalhs.core.domain.factory

object TokenGenerator {

    fun generate(
        size: Int = 8,
        uppercase: Boolean = true,
        lowercase: Boolean = false,
        numbers: Boolean = true
    ): String {

        val pool = buildList {
            if (uppercase) addAll("ABCDEFGHJKLMNPQRSTUVWXYZ".toList())
            if (lowercase) addAll("abcdefghijkmnopqrstuvwxyz".toList())
            if (numbers) addAll("23456789".toList())
        }

        require(pool.isNotEmpty()) {
            "At least one character type must be enabled"
        }

        return (1..size)
            .map { pool.random() }
            .joinToString("")
    }
}
