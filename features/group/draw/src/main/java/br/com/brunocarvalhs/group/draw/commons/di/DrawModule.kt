package br.com.brunocarvalhs.group.draw.commons.di

import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.draw.DrawInitializerImpl
import br.com.brunocarvalhs.group.draw.app.data.repository.DrawRepositoryImpl
import br.com.brunocarvalhs.group.draw.app.domain.repository.DrawRepository
import br.com.brunocarvalhs.group.draw.commons.analytics.DrawAnalytics
import br.com.brunocarvalhs.group.draw.commons.analytics.DrawAnalyticsImpl
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DrawModule {

    @Binds
    @IntoSet
    abstract fun bindDrawInitializer(
        impl: DrawInitializerImpl
    ): FeatureInitializer

    @Binds
    abstract fun bindDrawRepository(
        impl: DrawRepositoryImpl
    ): DrawRepository

    companion object {

        @Provides
        fun provideDrawAnalytics(
            firebaseAnalytics: FirebaseAnalytics
        ): DrawAnalytics {
            return DrawAnalyticsImpl(firebaseAnalytics)
        }
    }
}