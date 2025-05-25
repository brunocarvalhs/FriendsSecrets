package br.com.brunocarvalhs.friendssecrets.common.extensions

fun String.toMaskedPhoneNumber(): String {
    // Remove tudo que não for número
    val cleaned = this.filter { it.isDigit() }

    return if (cleaned.length > 8) {
        val prefix = cleaned.take(6) // +DDDDDD (ex: +5511)
        val masked = "******"
        val suffix = cleaned.takeLast(2)
        "+$prefix$masked$suffix"
    } else {
        "+$cleaned"
    }
}
