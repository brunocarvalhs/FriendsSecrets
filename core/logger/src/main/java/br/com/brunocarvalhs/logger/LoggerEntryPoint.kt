package br.com.brunocarvalhs.logger

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LoggerEntryPoint {
    fun crashlyticsLogger(): CrashlyticsLogger
}
