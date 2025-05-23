package br.com.brunocarvalhs.friendssecrets.commons.extensions

import com.google.firebase.perf.metrics.AddTrace
import kotlin.random.Random

@AddTrace(name = "Random.token")
fun Random.token(size: Int = 6): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..size)
        .map { charPool[Random.nextInt(charPool.size)] }
        .joinToString("")
}