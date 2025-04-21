package br.com.brunocarvalhs.friendssecrets.domain.base

import br.com.brunocarvalhs.friendssecrets.commons.coroutines.CoroutineDispatcherProvider
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import kotlinx.coroutines.withContext

/**
 * Classe base para todos os casos de uso.
 * Fornece rastreamento de desempenho e gerenciamento de dispatchers de coroutine.
 */
abstract class BaseUseCase<in P, R> {

    // Estas propriedades serão injetadas pelo ServiceLocator
    lateinit var dispatchers: CoroutineDispatcherProvider
    lateinit var performanceManager: PerformanceManager

    /**
     * Sobrescreva isso para definir o nome único do trace para rastreamento de desempenho
     */
    abstract val traceName: String

    /**
     * Executa o caso de uso com rastreamento de desempenho
     */
    protected abstract suspend fun execute(parameters: P): Result<R>

    /**
     * Invoca o caso de uso com rastreamento de desempenho
     */
    suspend operator fun invoke(parameters: P): Result<R> {
        return withContext(dispatchers.io) {
            performanceManager.startTrace(traceName)
            try {
                execute(parameters).also {
                    performanceManager.stopTrace(traceName)
                }
            } catch (e: Exception) {
                performanceManager.stopTrace(traceName)
                Result.failure(e)
            }
        }
    }
}

/**
 * Classe base para casos de uso que não requerem parâmetros
 */
abstract class BaseUseCaseNoParams<R> {

    // Estas propriedades serão injetadas pelo ServiceLocator
    lateinit var dispatchers: CoroutineDispatcherProvider
    lateinit var performanceManager: PerformanceManager

    /**
     * Sobrescreva isso para definir o nome único do trace para rastreamento de desempenho
     */
    abstract val traceName: String

    /**
     * Executa o caso de uso com rastreamento de desempenho
     */
    protected abstract suspend fun execute(): Result<R>

    /**
     * Invoca o caso de uso com rastreamento de desempenho
     */
    suspend operator fun invoke(): Result<R> {
        return withContext(dispatchers.io) {
            performanceManager.startTrace(traceName)
            try {
                execute().also {
                    performanceManager.stopTrace(traceName)
                }
            } catch (e: Exception) {
                performanceManager.stopTrace(traceName)
                Result.failure(e)
            }
        }
    }
}
