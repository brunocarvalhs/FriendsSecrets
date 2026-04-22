package br.com.brunocarvalhs.friendssecrets.core.network.di

import br.com.brunocarvalhs.friendssecrets.core.network.data.NetworkManager
import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindNetworkService(impl: NetworkManager): NetworkService

    companion object {
        @Provides
        @Singleton
        fun provideFirestore(): FirebaseFirestore = Firebase.firestore
    }
}