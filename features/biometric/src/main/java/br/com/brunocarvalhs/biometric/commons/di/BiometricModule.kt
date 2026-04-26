package br.com.brunocarvalhs.biometric.commons.di

import br.com.brunocarvalhs.biometric.BiometricInitializerImpl
import br.com.brunocarvalhs.core.navigation.FeatureInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class BiometricModule {

    @Binds
    @IntoSet
    abstract fun bindBiometricInitializer(impl: BiometricInitializerImpl): FeatureInitializer
}
