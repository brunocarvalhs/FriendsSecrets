package br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.commons.extensions.getId
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsInitialization : Initializer<FirebaseAnalytics> {

    override fun create(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context).apply {
            setUserId(context.getId())
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(
            FirebaseAppInitialization::class.java
        )
    }
}