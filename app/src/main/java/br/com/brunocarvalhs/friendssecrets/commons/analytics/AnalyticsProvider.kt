package br.com.brunocarvalhs.friendssecrets.commons.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object AnalyticsProvider {
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun track(event: Event, params: Map<Param, String>) {
        firebaseAnalytics.logEvent(event.value, Bundle().apply {
            params.forEach { (key, value) -> putString(key.value, value) }
        })
    }

    enum class Event(val value: String) {
        VISUALIZATION(FirebaseAnalytics.Event.SCREEN_VIEW)
    }

    enum class Param(val value: String) {
        SCREEN_NAME(FirebaseAnalytics.Param.SCREEN_NAME),
        SCREEN_DISPLAY_NAME(FirebaseAnalytics.Param.SCREEN_CLASS)
    }
}