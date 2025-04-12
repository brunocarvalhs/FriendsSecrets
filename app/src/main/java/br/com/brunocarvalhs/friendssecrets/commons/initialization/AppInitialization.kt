package br.com.brunocarvalhs.friendssecrets.commons.initialization

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import timber.log.Timber

abstract class AppInitialization {

    private val performanceManager: PerformanceManager = PerformanceManager()

    abstract fun execute()

    abstract fun tag(): String

    fun start() {
        performanceManager.start(tag())
        Timber.d("Starting initialization: ${tag()}")
    }

    fun stop() {
        performanceManager.stop(tag())
        Timber.d("Stopping initialization: ${tag()}")
    }
}