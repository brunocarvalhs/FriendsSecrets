package br.com.brunocarvalhs.friendssecrets.data.extensions

import kotlin.random.Random

internal fun Random.token(size: Int = 6): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..size)
        .map { charPool[Random.nextInt(charPool.size)] }
        .joinToString("")
}