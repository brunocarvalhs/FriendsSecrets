package br.com.brunocarvalhs.core.review.di

import br.com.brunocarvalhs.core.review.data.ReviewPromptManager
import br.com.brunocarvalhs.core.review.domain.ReviewPromptService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReviewModule {

    @Binds
    @Singleton
    internal abstract fun bindReviewPromptService(impl: ReviewPromptManager): ReviewPromptService
}
