package com.wirajasa.wirajasabisnis.di

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wirajasa.wirajasabisnis.data.repository.*
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
        return AuthRepositoryImpl(mContext, Firebase.auth, Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideProductRepository(@ApplicationContext mContext: Context): ProductRepository {
        val auth = Firebase.auth
        val storage = Firebase.storage.reference
        val firestoreDb = Firebase.firestore
        return ProductRepositoryImpl(mContext, auth,storage, firestoreDb,Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideStorageRepository(@ApplicationContext mContext: Context): StorageRepository {
        return StorageRepositoryImpl(Firebase.storage, mContext, Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideUserRepository(@ApplicationContext mContext: Context, authRepository: AuthRepository) : UserRepository {
        return UserRepositoryImpl(Firebase.storage, Firebase.firestore, authRepository, mContext, Dispatchers.IO)
    }
}