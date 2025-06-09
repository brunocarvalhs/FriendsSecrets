package br.com.brunocarvalhs.friendssecrets.data.initialization

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.common.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.common.extensions.getId
import br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics.CrashlyticsProvider
import br.com.brunocarvalhs.friendssecrets.common.performance.PerformanceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitializer @Inject constructor(
    private val analytics: AnalyticsProvider.AnalyticsEvent,
    private val crashlytics: CrashlyticsProvider.CrashlyticsEvent,
    private val performance: PerformanceManager.PerformanceEvent,
    @ApplicationContext private val context: Context
) {
    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                analytics.setUserId(context.getId())
                crashlytics.setUserId(context.getId())
                performance.setDeviceId(context.getId())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
