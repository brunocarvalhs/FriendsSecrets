package br.com.brunocarvalhs.friendssecrets.data.di

import br.com.brunocarvalhs.friendssecrets.data.repository.GroupRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.repository.UserRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.service.ContactServiceImpl
import br.com.brunocarvalhs.friendssecrets.data.service.DrawServiceImpl
import br.com.brunocarvalhs.friendssecrets.domain.repositories.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.repositories.UserRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.ContactService
import br.com.brunocarvalhs.friendssecrets.domain.services.DrawService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindsModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindGroupRepository(repository: GroupRepositoryImpl): GroupRepository

    @Binds
    @Singleton
    abstract fun bindContactService(service: ContactServiceImpl): ContactService

    @Binds
    @Singleton
    abstract fun bindDrawService(service: DrawServiceImpl): DrawService
}