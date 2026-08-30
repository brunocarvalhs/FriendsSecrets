package br.com.brunocarvalhs.group.details.commons.di

import br.com.brunocarvalhs.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.details.GroupDetailsInitializerImpl
import br.com.brunocarvalhs.group.details.app.data.repository.GiftSuggestionRepositoryImpl
import br.com.brunocarvalhs.group.details.app.data.repository.GroupDetailsRepositoryImpl
import br.com.brunocarvalhs.group.details.app.domain.repository.GiftSuggestionRepository
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import com.google.firebase.functions.FirebaseFunctions
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

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

    @Binds
    internal abstract fun bindGiftSuggestionRepository(
        impl: GiftSuggestionRepositoryImpl
    ): GiftSuggestionRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseFunctions(): FirebaseFunctions = FirebaseFunctions.getInstance()
    }
}
