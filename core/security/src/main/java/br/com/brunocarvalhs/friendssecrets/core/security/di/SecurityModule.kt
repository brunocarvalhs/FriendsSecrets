package br.com.brunocarvalhs.friendssecrets.core.security.di

import br.com.brunocarvalhs.friendssecrets.core.security.data.CryptoManager
import br.com.brunocarvalhs.friendssecrets.core.security.domain.CryptoService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {

    @Binds
    @Singleton
    abstract fun bindCryptoService(impl: CryptoManager): CryptoService
}
