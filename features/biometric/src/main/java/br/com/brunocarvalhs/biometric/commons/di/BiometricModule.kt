package br.com.brunocarvalhs.biometric.commons.di

import br.com.brunocarvalhs.biometric.BiometricInitializerImpl
import br.com.brunocarvalhs.biometric.commons.analytics.BiometricAnalytics
import br.com.brunocarvalhs.biometric.commons.analytics.BiometricAnalyticsImpl
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class BiometricModule {

    @Binds
    @IntoSet
    abstract fun bindBiometricInitializer(impl: BiometricInitializerImpl): FeatureInitializer

    companion object {
        @Provides
        internal fun provideBiometricAnalytics(
            firebaseAnalytics: FirebaseAnalytics
        ): BiometricAnalytics {
            return BiometricAnalyticsImpl(firebaseAnalytics)
        }
    }
}