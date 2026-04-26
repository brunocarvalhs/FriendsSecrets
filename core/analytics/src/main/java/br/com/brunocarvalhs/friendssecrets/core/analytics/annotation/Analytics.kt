package br.com.brunocarvalhs.friendssecrets.core.analytics.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Analytics(
    val event: String,
    val params: Array<Param> = []
)

annotation class Param(
    val key: String,
    val value: String
)
