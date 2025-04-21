package br.com.brunocarvalhs.friendssecrets.di

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.commons.coroutines.CoroutineDispatcherProvider
import br.com.brunocarvalhs.friendssecrets.commons.initialization.AppInitializationManager
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.AnalyticsInitialization
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.CrashlyticsInitialization
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.RemoteInitialization
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.TimberInitialization
import br.com.brunocarvalhs.friendssecrets.commons.logger.CrashLoggerProvider
import br.com.brunocarvalhs.friendssecrets.commons.logger.crashlytics.CrashlyticsProvider
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.commons.remote.RemoteProvider
import br.com.brunocarvalhs.friendssecrets.commons.remote.theme.ThemeRemoteProvider
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.commons.security.BiometricManager
import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.commons.theme.ThemeManager
import br.com.brunocarvalhs.friendssecrets.data.repository.GroupRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.repository.UserRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.service.AuthService
import br.com.brunocarvalhs.friendssecrets.data.service.ContactService
import br.com.brunocarvalhs.friendssecrets.data.service.DrawService
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

/**
 * Service Locator para gerenciar dependências de forma manual.
 * Esta classe fornece acesso centralizado a todas as instâncias necessárias no aplicativo.
 */
object ServiceLocator {
    
    private var applicationContext: Context? = null
    
    fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }
    
    // Verifica se o contexto foi inicializado
    private fun requireContext(): Context {
        return applicationContext ?: throw IllegalStateException(
            "ServiceLocator não foi inicializado. Chame initialize() primeiro."
        )
    }
    
    // Instâncias únicas (Singletons)
    private val coroutineDispatcherProvider by lazy { CoroutineDispatcherProvider() }
    private val performanceManager by lazy { PerformanceManager() }
    private val cryptoService by lazy { CryptoService() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val gson by lazy { Gson() }
    private val remoteProvider by lazy { RemoteProvider() }
    private val analyticsProvider by lazy { AnalyticsProvider() }
    private val crashLoggerProvider by lazy { CrashlyticsProvider() }
    
    // Serviços
    private val authService by lazy { AuthService() }
    private val storageService by lazy { StorageService() }
    private val contactService by lazy { ContactService(requireContext()) }
    private val generativeService by lazy { GenerativeService() }
    private val drawService by lazy { DrawService(cryptoService) }
    
    // Gerenciadores
    private val biometricManager by lazy { BiometricManager(requireContext()) }
    private val themeManager by lazy { ThemeManager(requireContext()) }
    private val toggleManager by lazy { ToggleManager(requireContext(), remoteProvider) }
    private val themeRemoteProvider by lazy { ThemeRemoteProvider(requireContext(), remoteProvider, gson) }
    
    // Repositórios
    private val groupRepository: GroupRepository by lazy { 
        GroupRepositoryImpl(firestore, cryptoService, drawService) 
    }
    private val userRepository: UserRepository by lazy { 
        UserRepositoryImpl(firestore, cryptoService) 
    }
    
    // Inicialização do App
    val appInitializationManager by lazy {
        AppInitializationManager(
            listOf(
                RemoteInitialization(),
                TimberInitialization(),
                CrashlyticsInitialization(requireContext()),
                AnalyticsInitialization(requireContext())
            )
        )
    }
    
    // Use Cases
    val createProfileUseCase by lazy { 
        CreateProfileUseCase(userRepository, storageService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val deleteAccountUseCase by lazy { 
        DeleteAccountUseCase(userRepository, authService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val drawRevelationUseCase by lazy { 
        DrawRevelationUseCase(groupRepository, storageService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val getLikesProfileUseCase by lazy { 
        GetLikesProfileUseCase(userRepository, storageService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val getListUsersByContactUseCase by lazy { 
        GetListUsersByContactUseCase(userRepository, contactService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val groupByTokenUseCase by lazy { 
        GroupByTokenUseCase(groupRepository, storageService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val groupCreateUseCase by lazy { 
        GroupCreateUseCase(groupRepository, storageService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val groupDeleteUseCase by lazy { 
        GroupDeleteUseCase(groupRepository, storageService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val groupDrawUseCase by lazy { 
        GroupDrawUseCase(groupRepository, storageService, generativeService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val groupEditUseCase by lazy { 
        GroupEditUseCase(groupRepository, storageService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val groupExitUseCase by lazy { 
        GroupExitUseCase(groupRepository, storageService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val groupListUseCase by lazy { 
        GroupListUseCase(groupRepository, storageService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val groupReadUseCase by lazy { 
        GroupReadUseCase(groupRepository, storageService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val logoutUseCase by lazy { LogoutUseCase() }
    
    val sendPhoneUseCase by lazy { 
        SendPhoneUseCase(authService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    val verifyPhoneUseCase by lazy { 
        VerifyPhoneUseCase(authService).apply {
            this.dispatchers = coroutineDispatcherProvider
            this.performanceManager = this@ServiceLocator.performanceManager
        }
    }
    
    // Getters para componentes de UI
    fun getAnalyticsProvider() = analyticsProvider
    fun getToggleManager() = toggleManager
    fun getThemeRemoteProvider() = themeRemoteProvider
    fun getBiometricManager() = biometricManager
    fun getThemeManager() = themeManager
    fun getCoroutineDispatcherProvider() = coroutineDispatcherProvider
}