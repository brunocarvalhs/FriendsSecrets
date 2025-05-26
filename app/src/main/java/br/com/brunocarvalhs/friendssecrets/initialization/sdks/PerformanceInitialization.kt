package br.com.brunocarvalhs.friendssecrets.initialization.sdks

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.common.extensions.getId
import br.com.brunocarvalhs.friendssecrets.common.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.initialization.providers.PerformanceEventImpl
import com.google.firebase.perf.FirebasePerformance

class PerformanceInitialization : Initializer<PerformanceManager> {

    override fun create(context: Context): PerformanceManager {
        val firebasePerformance = FirebasePerformance.getInstance().apply {
            putAttribute("deviceId", context.getId())
        }
        PerformanceManager.initialize(PerformanceEventImpl(firebasePerformance))
        return PerformanceManager.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(
            FirebaseAppInitialization::class.java
        )
    }
}