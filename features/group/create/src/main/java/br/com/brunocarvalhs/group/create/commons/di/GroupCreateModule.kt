package br.com.brunocarvalhs.group.create.commons.di

import br.com.brunocarvalhs.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.create.GroupCreateInitializerImpl
import br.com.brunocarvalhs.group.create.app.data.repository.ContactsRepositoryImpl
import br.com.brunocarvalhs.group.create.app.data.repository.GroupCreateRepositoryImpl
import br.com.brunocarvalhs.group.create.app.data.services.ContactServiceImpl
import br.com.brunocarvalhs.group.create.app.data.services.GroupImageManager
import br.com.brunocarvalhs.group.create.app.domain.repositories.ContactsRepository
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository
import br.com.brunocarvalhs.group.create.app.domain.services.ContactService
import br.com.brunocarvalhs.group.create.app.domain.services.GroupImageService
import br.com.brunocarvalhs.group.create.commons.analytics.GroupCreateAnalytics
import br.com.brunocarvalhs.group.create.commons.analytics.GroupCreateAnalyticsImpl
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal abstract class GroupCreateModule {

    @Binds
    @IntoSet
    abstract fun bindInitializer(
        impl: GroupCreateInitializerImpl
    ): FeatureInitializer

    @Binds
    abstract fun bindContactsRepository(
        impl: ContactsRepositoryImpl
    ): ContactsRepository

    @Binds
    abstract fun bindGroupCreateRepository(
        impl: GroupCreateRepositoryImpl
    ): GroupCreateRepository

    @Binds
    abstract fun bindContactService(
        impl: ContactServiceImpl
    ): ContactService

    @Binds
    abstract fun bindGroupImageService(
        impl: GroupImageManager
    ): GroupImageService

    companion object {

        @Provides
        fun provideGroupCreateAnalytics(
            firebaseAnalytics: FirebaseAnalytics
        ): GroupCreateAnalytics {
            return GroupCreateAnalyticsImpl(firebaseAnalytics)
        }
    }
}
