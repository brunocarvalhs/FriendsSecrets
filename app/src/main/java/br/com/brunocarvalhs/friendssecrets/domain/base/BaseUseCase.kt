package br.com.brunocarvalhs.friendssecrets.domain.base

import br.com.brunocarvalhs.friendssecrets.commons.coroutines.CoroutineDispatcherProvider
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Base class for all use cases.
 * Provides performance tracking and coroutine dispatcher management.
 */
abstract class BaseUseCase<in P, R> {

    @Inject
    lateinit var dispatchers: CoroutineDispatcherProvider

    @Inject
    lateinit var performanceManager: PerformanceManager

    /**
     * Override this to set the unique trace name for performance tracking
     */
    abstract val traceName: String

    /**
     * Executes the use case with performance tracking
     */
    protected abstract suspend fun execute(parameters: P): Result<R>

    /**
     * Invokes the use case with performance tracking
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
 * Base class for use cases that don't require parameters
 */
abstract class BaseUseCaseNoParams<R> {

    @Inject
    lateinit var dispatchers: CoroutineDispatcherProvider

    @Inject
    lateinit var performanceManager: PerformanceManager

    /**
     * Override this to set the unique trace name for performance tracking
     */
    abstract val traceName: String

    /**
     * Executes the use case with performance tracking
     */
    protected abstract suspend fun execute(): Result<R>

    /**
     * Invokes the use case with performance tracking
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