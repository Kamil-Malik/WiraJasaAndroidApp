package com.wirajasa.wirajasabisnis.di

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wirajasa.wirajasabisnis.data.repository.AuthRepository
import com.wirajasa.wirajasabisnis.data.repository.AuthRepositoryImpl
import com.wirajasa.wirajasabisnis.data.repository.ProductRepository
import com.wirajasa.wirajasabisnis.data.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(@ApplicationContext mContext: Context): AuthRepository {
        return AuthRepositoryImpl(mContext, Firebase.auth, Firebase.firestore, Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideAuthRepository2(@ApplicationContext mContext: Context): ProductRepository {
        val auth = Firebase.auth
        val storage = Firebase.storage.reference
        val firestoreDb = Firebase.firestore
        return ProductRepositoryImpl(mContext, auth,storage, firestoreDb,Dispatchers.IO)
    }
}