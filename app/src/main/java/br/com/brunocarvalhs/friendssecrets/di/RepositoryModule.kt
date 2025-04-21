package br.com.brunocarvalhs.friendssecrets.di

import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.data.repository.GroupRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.repository.UserRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.service.DrawService
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDrawService(cryptoService: CryptoService): DrawService {
        return DrawService(cryptoService = cryptoService)
    }

    @Provides
    @Singleton
    fun provideGroupRepository(
        firestore: FirebaseFirestore,
        cryptoService: CryptoService,
        drawService: DrawService
    ): GroupRepository {
        return GroupRepositoryImpl(
            firestore = firestore,
            cryptoService = cryptoService,
            drawService = drawService
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore,
        cryptoService: CryptoService
    ): UserRepository {
        return UserRepositoryImpl(firestore = firestore, cryptoService = cryptoService)
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}