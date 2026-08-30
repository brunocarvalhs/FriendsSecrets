package br.com.brunocarvalhs.core.notifications.di

import br.com.brunocarvalhs.core.notifications.data.PushTokenRepositoryImpl
import br.com.brunocarvalhs.core.notifications.domain.PushTokenRepository
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NotificationsModule {

    @Binds
    internal abstract fun bindPushTokenRepository(
        impl: PushTokenRepositoryImpl
    ): PushTokenRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()
    }
}
