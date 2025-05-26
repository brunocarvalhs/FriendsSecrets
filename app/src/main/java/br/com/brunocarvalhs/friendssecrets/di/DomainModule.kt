package br.com.brunocarvalhs.friendssecrets.di

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.repositories.UserRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.ContactService
import br.com.brunocarvalhs.friendssecrets.domain.services.CryptoService
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.PhoneAuthService
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.useCases.CreateProfileUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.DeleteAccountUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.DrawRevelationUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GetLikesProfileUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GetListUsersByContactUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupByTokenUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupCreateUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupDeleteUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupDrawUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupEditUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupExitUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupListUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupReadUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.LogoutUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.SendPhoneUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.VerifyPhoneUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideCreateProfileUseCase(
        session: SessionService<UserEntities>,
        repository: UserRepository,
        performance: PerformanceService
    ) = CreateProfileUseCase(session, repository, performance)

    @Provides
    fun provideDeleteAccountUseCase(
        repository: UserRepository,
        session: SessionService<UserEntities>,
        performance: PerformanceService
    ) = DeleteAccountUseCase(repository, session, performance)

    @Provides
    fun provideDrawRevelationUseCase(
        repository: GroupRepository,
        storage: StorageService,
        crypto: CryptoService,
        performance: PerformanceService
    ) = DrawRevelationUseCase(repository, storage, crypto, performance)

    @Provides
    fun provideGetLikesProfileUseCase(
        session: SessionService<UserEntities>,
        repository: UserRepository,
        performance: PerformanceService
    ) = GetLikesProfileUseCase(session, repository, performance)

    @Provides
    fun provideGetListUsersByContactUseCase(
        repository: UserRepository,
        contact: ContactService,
        performance: PerformanceService
    ) = GetListUsersByContactUseCase(repository, contact, performance)

    @Provides
    fun provideGroupByTokenUseCase(
        repository: GroupRepository,
        storage: StorageService,
        performance: PerformanceService
    ) = GroupByTokenUseCase(repository, storage, performance)

    @Provides
    fun provideGroupCreateUseCase(
        repository: GroupRepository,
        storage: StorageService,
        performance: PerformanceService
    ) = GroupCreateUseCase(repository, storage, performance)

    @Provides
    fun provideGroupDeleteUseCase(
        repository: GroupRepository,
        storage: StorageService,
        performance: PerformanceService
    ) = GroupDeleteUseCase(repository, storage, performance)

    @Provides
    fun provideGroupDrawUseCase(
        repository: GroupRepository,
        performance: PerformanceService
    ) = GroupDrawUseCase(repository, performance)

    @Provides
    fun provideGroupEditUseCase(
        repository: GroupRepository,
        performance: PerformanceService
    ) = GroupEditUseCase(repository, performance)

    @Provides
    fun provideGroupExitUseCase(
        repository: GroupRepository,
        storage: StorageService,
        performance: PerformanceService
    ) = GroupExitUseCase(repository, storage, performance)

    @Provides
    fun provideGroupListUseCase(
        repository: GroupRepository,
        storage: StorageService,
        performance: PerformanceService
    ) = GroupListUseCase(repository, storage, performance)

    @Provides
    fun provideGroupReadUseCase(
        repository: GroupRepository,
        storage: StorageService,
        performance: PerformanceService
    ) = GroupReadUseCase(repository, storage, performance)

    @Provides
    fun provideLogoutUseCase(
        session: SessionService<UserEntities>,
        performance: PerformanceService
    ) = LogoutUseCase(session, performance)

    @Provides
    fun provideSendPhoneUseCase(
        phoneAuthService: PhoneAuthService,
        performance: PerformanceService
    ) = SendPhoneUseCase(phoneAuthService, performance)

    @Provides
    fun provideVerifyPhoneUseCase(
        phoneAuthService: PhoneAuthService,
        performance: PerformanceService
    ) = VerifyPhoneUseCase(phoneAuthService, performance)
}
