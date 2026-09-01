package br.com.brunocarvalhs.friendssecrets.initializers

import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

internal fun provideAppCheckProviderFactory(): AppCheckProviderFactory =
    PlayIntegrityAppCheckProviderFactory.getInstance()
