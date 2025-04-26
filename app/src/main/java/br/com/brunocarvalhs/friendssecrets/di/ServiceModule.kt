package br.com.brunocarvalhs.friendssecrets.di

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.data.service.AuthService
import br.com.brunocarvalhs.friendssecrets.data.service.ContactService
import br.com.brunocarvalhs.friendssecrets.data.service.GenerativeService
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return AuthService()
    }

    @Provides
    @Singleton
    fun provideContactService(): ContactService {
        return ContactService()
    }

    @Provides
    @Singleton
    fun provideGenerativeService(): GenerativeService {
        return GenerativeService()
    }

    @Provides
    @Singleton
    fun provideStorageService(): StorageService {
        return StorageService()
    }
}