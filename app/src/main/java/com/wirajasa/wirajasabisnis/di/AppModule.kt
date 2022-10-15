package com.wirajasa.wirajasabisnis.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.data.repository.AuthRepository
import com.wirajasa.wirajasabisnis.data.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository() : AuthRepository {
        val auth = Firebase.auth
        return AuthRepositoryImpl(auth)
    }
}