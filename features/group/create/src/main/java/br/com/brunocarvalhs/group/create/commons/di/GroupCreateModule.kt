package br.com.brunocarvalhs.group.create.commons.di

import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.create.GroupCreateInitializerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupCreateModule {

    @Binds
    @IntoSet
    abstract fun bindGroupCreateInitializer(impl: GroupCreateInitializerImpl): FeatureInitializer
}