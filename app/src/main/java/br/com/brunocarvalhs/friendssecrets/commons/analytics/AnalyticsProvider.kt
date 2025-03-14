package br.com.brunocarvalhs.friendssecrets.commons.analytics

import android.content.Context
import android.os.Bundle
import android.provider.Settings.Secure
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object AnalyticsProvider {
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun track(event: AnalyticsEvents, params: Map<AnalyticsParams, String> = emptyMap()) {
        firebaseAnalytics.logEvent(event.value, Bundle().apply {
            params.forEach { (key, value) -> putString(key.value, value) }
        })
    }

    fun setUserId(context: Context) {
        val deviceId =
            Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        firebaseAnalytics.setUserId(deviceId)
    }
}
