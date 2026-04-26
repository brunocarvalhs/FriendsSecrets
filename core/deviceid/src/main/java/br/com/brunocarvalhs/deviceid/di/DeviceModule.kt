package br.com.brunocarvalhs.deviceid.di

import br.com.brunocarvalhs.deviceid.DeviceManager
import br.com.brunocarvalhs.deviceid.DeviceService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DeviceModule {

    @Binds
    @Singleton
    abstract fun bindDeviceManager(
        impl: DeviceManager
    ): DeviceService
}
