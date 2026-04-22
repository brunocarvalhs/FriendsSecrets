package br.com.brunocarvalhs.biometric.di

import br.com.brunocarvalhs.biometric.BiometricManager
import br.com.brunocarvalhs.biometric.BiometricService
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
    abstract fun bindBiometricManager(
        impl: BiometricManager
    ): BiometricService
}