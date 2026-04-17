package br.com.brunocarvalhs.settings.app.list

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SettingsModule {
    @Binds
    @Singleton
    abstract fun bindSettingsAnalytics(impl: SettingsAnalyticsImpl): SettingsAnalytics
}
