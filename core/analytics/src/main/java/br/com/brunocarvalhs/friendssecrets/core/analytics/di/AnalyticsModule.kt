package br.com.brunocarvalhs.friendssecrets.core.analytics.di

import br.com.brunocarvalhs.friendssecrets.core.analytics.AnalyticsService
import br.com.brunocarvalhs.friendssecrets.core.analytics.FirebaseAnalyticsManager
import br.com.brunocarvalhs.friendssecrets.core.analytics.aspect.AnalyticsAspect
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsService(
        analyticsManager: FirebaseAnalyticsManager
    ): AnalyticsService

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

        @Provides
        @Singleton
        fun provideAnalyticsAspect(analyticsService: AnalyticsService): AnalyticsAspect {
            val aspect = AnalyticsAspect()
            AnalyticsAspect.setAnalyticsService(analyticsService)
            return aspect
        }
    }
}
