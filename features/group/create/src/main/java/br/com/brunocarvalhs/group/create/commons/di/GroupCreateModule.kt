package br.com.brunocarvalhs.group.create.commons.di

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
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class GroupCreateModule {

    @Binds
    @Singleton
    abstract fun bindContactService(
        impl: ContactServiceImpl
    ): ContactService

    @Binds
    abstract fun bindContactsRepository(
        impl: ContactsRepositoryImpl
    ): ContactsRepository

    @Binds
    abstract fun bindGroupCreateRepository(
        impl: GroupCreateRepositoryImpl
    ): GroupCreateRepository

    @Binds
    @Singleton
    abstract fun bindGroupCreateAnalytics(
        impl: GroupCreateAnalyticsImpl
    ): GroupCreateAnalytics

    @Binds
    @Singleton
    abstract fun bindGroupImageService(
        impl: GroupImageManager
    ): GroupImageService
}
