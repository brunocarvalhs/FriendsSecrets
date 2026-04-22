package br.com.brunocarvalhs.friendssecrets.core.infrastructure.initializer

import android.app.Application
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import timber.log.Timber
import javax.inject.Inject

class FirebaseInitializerImpl @Inject constructor(
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics,
    private val performance: FirebasePerformance
) : Ini {

    override fun init(application: Application) {
        Timber.d("Initializing Firebase Services")
        analytics.setAnalyticsCollectionEnabled(true)
        crashlytics.setCrashlyticsCollectionEnabled(true)
        performance.isPerformanceCollectionEnabled = true
    }

}
