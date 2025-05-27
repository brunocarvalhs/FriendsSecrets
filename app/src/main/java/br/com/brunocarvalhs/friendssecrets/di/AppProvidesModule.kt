package br.com.brunocarvalhs.friendssecrets.di // Ou pacote apropriado no :app

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.common.extensions.getId
import br.com.brunocarvalhs.friendssecrets.common.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.common.session.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.initialization.providers.PerformanceEventImpl
import br.com.brunocarvalhs.friendssecrets.initialization.providers.SessionEventImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.perf.FirebasePerformance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppProvidesModule {

    @Provides
    @Singleton
    fun providePerformanceEvent(firebasePerformance: FirebasePerformance): PerformanceManager.PerformanceEvent {
        return PerformanceEventImpl(firebasePerformance)
    }

    @Provides
    @Singleton
    fun provideSessionEvent(firebaseAuth: FirebaseAuth): SessionManager.SessionEvent<UserEntities> {
        return SessionEventImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFirebasePerformance(@ApplicationContext context: Context): FirebasePerformance {
        return FirebasePerformance.getInstance().apply {
            putAttribute("deviceId", context.getId())
        }
    }
}
