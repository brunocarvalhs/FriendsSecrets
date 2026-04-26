package br.com.brunocarvalhs.friendssecrets.initializers

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import com.google.firebase.perf.FirebasePerformance

class PerformanceInitializer : Initializer<FirebasePerformance> {

    override fun create(context: Context): FirebasePerformance {
        return FirebasePerformance.getInstance().apply {
            isPerformanceCollectionEnabled = !BuildConfig.DEBUG
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return mutableListOf(FirebaseInitializer::class.java)
    }
}
