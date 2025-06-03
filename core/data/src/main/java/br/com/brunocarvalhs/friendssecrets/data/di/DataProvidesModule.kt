package br.com.brunocarvalhs.friendssecrets.data.di

import br.com.brunocarvalhs.friendssecrets.data.BuildConfig
import br.com.brunocarvalhs.friendssecrets.data.service.GenerativeServiceImpl
import br.com.brunocarvalhs.friendssecrets.data.service.PhoneAuthServiceImpl
import br.com.brunocarvalhs.friendssecrets.domain.services.GenerativeService
import br.com.brunocarvalhs.friendssecrets.domain.services.PhoneAuthService
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataProvidesModule {

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideFirebaseAuthentication(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideGenerativeModel(): GenerativeModel = GenerativeModel(
        BuildConfig.AI_API_KEY, BuildConfig.AI_MODEL_NAME
    )

    @Provides
    fun providePhoneAuthService(
        firebaseAuth: FirebaseAuth
    ): PhoneAuthService {
        return PhoneAuthServiceImpl(firebaseAuth)
    }

    @Provides
    fun provideGenerativeService(
        generativeModel: GenerativeModel
    ): GenerativeService {
        return GenerativeServiceImpl(generativeModel)
    }
}