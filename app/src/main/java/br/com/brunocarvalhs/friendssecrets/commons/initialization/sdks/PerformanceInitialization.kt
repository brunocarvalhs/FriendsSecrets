package br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.commons.extensions.getId
import com.google.firebase.perf.FirebasePerformance

class PerformanceInitialization: Initializer<FirebasePerformance> {

    override fun create(context: Context): FirebasePerformance {
        return FirebasePerformance.getInstance().apply {
            putAttribute("deviceId", context.getId())
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(
            FirebaseAppInitialization::class.java
        )
    }
}