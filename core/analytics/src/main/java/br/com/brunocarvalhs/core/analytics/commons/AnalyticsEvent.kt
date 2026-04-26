package br.com.brunocarvalhs.core.analytics.commons

enum class AnalyticsEvent(val value: String) {
    CLICK("click"),
    LONG_PRESS("long_press"),
    VIEW("view"),
    SUBMIT("submit"),
    NAVIGATE("navigate"),
    ERROR("error"),
}
