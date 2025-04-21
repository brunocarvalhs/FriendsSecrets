package br.com.brunocarvalhs.friendssecrets.di

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.AuthService
import br.com.brunocarvalhs.friendssecrets.data.service.ContactService
import br.com.brunocarvalhs.friendssecrets.data.service.GenerativeService
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
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
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideCreateProfileUseCase(
        userRepository: UserRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): CreateProfileUseCase {
        return CreateProfileUseCase(userRepository, storageService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideDeleteAccountUseCase(
        userRepository: UserRepository,
        authService: AuthService,
        performanceManager: PerformanceManager
    ): DeleteAccountUseCase {
        return DeleteAccountUseCase(userRepository, authService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideDrawRevelationUseCase(
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): DrawRevelationUseCase {
        return DrawRevelationUseCase(groupRepository, storageService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideGetLikesProfileUseCase(
        userRepository: UserRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): GetLikesProfileUseCase {
        return GetLikesProfileUseCase(userRepository, storageService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideGetListUsersByContactUseCase(
        userRepository: UserRepository,
        contactService: ContactService,
        performanceManager: PerformanceManager
    ): GetListUsersByContactUseCase {
        return GetListUsersByContactUseCase(userRepository, contactService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideGroupByTokenUseCase(
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): GroupByTokenUseCase {
        return GroupByTokenUseCase(groupRepository, storageService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideGroupCreateUseCase(
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): GroupCreateUseCase {
        return GroupCreateUseCase(groupRepository, storageService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideGroupDeleteUseCase(
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): GroupDeleteUseCase {
        return GroupDeleteUseCase(groupRepository, storageService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideGroupDrawUseCase(
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager,
        generativeService: GenerativeService
    ): GroupDrawUseCase {
        return GroupDrawUseCase(groupRepository, storageService, performanceManager, generativeService)
    }

    @Provides
    @Singleton
    fun provideGroupEditUseCase(
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): GroupEditUseCase {
        return GroupEditUseCase(groupRepository, storageService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideGroupExitUseCase(
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): GroupExitUseCase {
        return GroupExitUseCase(groupRepository, storageService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideGroupListUseCase(
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): GroupListUseCase {
        return GroupListUseCase(groupRepository, storageService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideGroupReadUseCase(
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): GroupReadUseCase {
        return GroupReadUseCase(groupRepository, storageService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(): LogoutUseCase {
        return LogoutUseCase()
    }

    @Provides
    @Singleton
    fun provideSendPhoneUseCase(
        authService: AuthService,
        performanceManager: PerformanceManager
    ): SendPhoneUseCase {
        return SendPhoneUseCase(authService, performanceManager)
    }

    @Provides
    @Singleton
    fun provideVerifyPhoneUseCase(
        authService: AuthService,
        performanceManager: PerformanceManager
    ): VerifyPhoneUseCase {
        return VerifyPhoneUseCase(authService, performanceManager)
    }
}