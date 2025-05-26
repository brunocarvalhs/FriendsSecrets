package br.com.brunocarvalhs.friendssecrets.data.di

import br.com.brunocarvalhs.friendssecrets.data.repository.GroupRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.repository.UserRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.service.ContactServiceImpl
import br.com.brunocarvalhs.friendssecrets.data.service.DrawServiceImpl
import br.com.brunocarvalhs.friendssecrets.data.service.GenerativeServiceImpl
import br.com.brunocarvalhs.friendssecrets.data.service.PhoneAuthServiceImpl
import br.com.brunocarvalhs.friendssecrets.domain.repositories.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.repositories.UserRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.ContactService
import br.com.brunocarvalhs.friendssecrets.domain.services.DrawService
import br.com.brunocarvalhs.friendssecrets.domain.services.GenerativeService
import br.com.brunocarvalhs.friendssecrets.domain.services.PhoneAuthService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindsModule {

    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindGroupRepository(repository: GroupRepositoryImpl): GroupRepository

    @Binds
    abstract fun bindContactService(service: ContactServiceImpl): ContactService

    @Binds
    abstract fun bindDrawService(service: DrawServiceImpl): DrawService
}
