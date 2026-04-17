package br.com.brunocarvalhs.biometric.commons.di

import br.com.brunocarvalhs.biometric.commons.analytics.BiometricAnalytics
import br.com.brunocarvalhs.biometric.commons.analytics.BiometricAnalyticsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BiometricModule {
    @Binds
    @Singleton
    abstract fun bindBiometricAnalytics(impl: BiometricAnalyticsImpl): BiometricAnalytics
}