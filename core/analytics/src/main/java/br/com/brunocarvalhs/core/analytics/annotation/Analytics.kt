package br.com.brunocarvalhs.core.analytics.annotation

import AnalyticsParameter
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Analytics(
    val event: AnalyticsEvent,
    val params: Array<Param> = []
)

annotation class Param(
    val key: AnalyticsParameter,
    val value: String
)
