package br.com.brunocarvalhs.core.analytics.annotation

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
