package br.com.brunocarvalhs.friendssecrets.di

import android.app.Activity
import android.content.Context
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.data.service.AuthService
import br.com.brunocarvalhs.friendssecrets.data.service.ContactService
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideCreateProfileUseCase(
        userRepository: UserRepository,
        sessionManager: SessionManager,
        performanceManager: PerformanceManager
    ): CreateProfileUseCase {
        return CreateProfileUseCase(
            userRepository = userRepository,
            sessionManager = sessionManager,
            performanceManager = performanceManager
        )
    }

    @Provides
    @Singleton
    fun provideDeleteAccountUseCase(
        userRepository: UserRepository,
        sessionManager: SessionManager,
        performanceManager: PerformanceManager
    ): DeleteAccountUseCase {
        return DeleteAccountUseCase(
            userRepository = userRepository,
            sessionManager = sessionManager,
            performanceManager = performanceManager
        )
    }

    @Provides
    @Singleton
    fun provideDrawRevelationUseCase(
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager,
        cryptoService: CryptoService
    ): DrawRevelationUseCase {
        return DrawRevelationUseCase(
            repository = groupRepository,
            storage = storageService,
            cryptoService = cryptoService,
            performance = performanceManager
        )
    }

    @Provides
    @Singleton
    fun provideGetLikesProfileUseCase(
        userRepository: UserRepository,
        sessionManager: SessionManager,
        performanceManager: PerformanceManager
    ): GetLikesProfileUseCase {
        return GetLikesProfileUseCase(
            sessionManager = sessionManager,
            userRepository = userRepository,
            performanceManager = performanceManager
        )
    }

    @Provides
    @Singleton
    fun provideGetListUsersByContactUseCase(
        userRepository: UserRepository,
        contactService: ContactService,
        performanceManager: PerformanceManager
    ): GetListUsersByContactUseCase {
        return GetListUsersByContactUseCase(
            userRepository = userRepository,
            contactService = contactService,
            performanceManager = performanceManager
        )
    }

    @Provides
    @Singleton
    fun provideGroupByTokenUseCase(
        groupRepository: GroupRepository,
        storageService: StorageService,
    ): GroupByTokenUseCase {
        return GroupByTokenUseCase(groupRepository = groupRepository, storage = storageService)
    }

    @Provides
    @Singleton
    fun provideGroupCreateUseCase(
        @ApplicationContext context: Context,
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): GroupCreateUseCase {
        return GroupCreateUseCase(
            context = context,
            groupRepository = groupRepository,
            storage = storageService,
            performance = performanceManager
        )
    }

    @Provides
    @Singleton
    fun provideGroupDeleteUseCase(
        @ApplicationContext context: Context,
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): GroupDeleteUseCase {
        return GroupDeleteUseCase(
            context = context,
            groupRepository = groupRepository,
            storage = storageService,
            performance = performanceManager
        )
    }

    @Provides
    @Singleton
    fun provideGroupDrawUseCase(
        @ApplicationContext context: Context,
        groupRepository: GroupRepository,
        performanceManager: PerformanceManager,
    ): GroupDrawUseCase {
        return GroupDrawUseCase(
            context = context,
            groupRepository = groupRepository,
            performance = performanceManager,
        )
    }

    @Provides
    @Singleton
    fun provideGroupEditUseCase(
        groupRepository: GroupRepository,
        performanceManager: PerformanceManager
    ): GroupEditUseCase {
        return GroupEditUseCase(groupRepository = groupRepository, performance = performanceManager)
    }

    @Provides
    @Singleton
    fun provideGroupExitUseCase(
        @ApplicationContext context: Context,
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): GroupExitUseCase {
        return GroupExitUseCase(
            context = context,
            groupRepository = groupRepository,
            storage = storageService,
            performance = performanceManager
        )
    }

    @Provides
    @Singleton
    fun provideGroupListUseCase(
        groupRepository: GroupRepository,
        storageService: StorageService,
    ): GroupListUseCase {
        return GroupListUseCase(groupRepository = groupRepository, storage = storageService)
    }

    @Provides
    @Singleton
    fun provideGroupReadUseCase(
        @ApplicationContext context: Context,
        groupRepository: GroupRepository,
        storageService: StorageService,
        performanceManager: PerformanceManager
    ): GroupReadUseCase {
        return GroupReadUseCase(
            context = context,
            groupRepository = groupRepository,
            storage = storageService,
            performance = performanceManager
        )
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        sessionManager: SessionManager
    ): LogoutUseCase {
        return LogoutUseCase(
            sessionManager = sessionManager
        )
    }

    @Provides
    @Singleton
    fun provideSendPhoneUseCase(
        @ApplicationContext activity: Activity,
        authService: AuthService,
    ): SendPhoneUseCase {
        return SendPhoneUseCase(activity = activity, authService = authService)
    }

    @Provides
    @Singleton
    fun provideVerifyPhoneUseCase(
        authService: AuthService,
    ): VerifyPhoneUseCase {
        return VerifyPhoneUseCase(authService = authService)
    }
}