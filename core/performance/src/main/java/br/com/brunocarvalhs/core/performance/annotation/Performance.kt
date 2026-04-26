package br.com.brunocarvalhs.core.performance.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Performance(
    val name: String,
)
