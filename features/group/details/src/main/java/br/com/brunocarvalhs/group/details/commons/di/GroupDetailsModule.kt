package br.com.brunocarvalhs.group.details.commons.di

import br.com.brunocarvalhs.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.details.GroupDetailsInitializerImpl
import br.com.brunocarvalhs.group.details.app.data.repository.GroupDetailsRepositoryImpl
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupDetailsModule {

    @Binds
    @IntoSet
    abstract fun bindGroupDetailsInitializer(impl: GroupDetailsInitializerImpl): FeatureInitializer

    @Binds
    internal abstract fun bindGroupDetailsRepository(
        impl: GroupDetailsRepositoryImpl
    ): GroupDetailsRepository
}
