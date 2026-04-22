package br.com.brunocarvalhs.friendssecrets.core.analytics.di

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.core.analytics.AnalyticsService
import br.com.brunocarvalhs.friendssecrets.core.analytics.FirebaseAnalyticsManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    }
}
