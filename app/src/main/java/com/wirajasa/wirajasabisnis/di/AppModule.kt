package com.wirajasa.wirajasabisnis.di

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wirajasa.wirajasabisnis.feature_admin.domain.repository.AdminRepository
import com.wirajasa.wirajasabisnis.feature_admin.domain.repository_impl.AdminRepositoryImpl
import com.wirajasa.wirajasabisnis.core.crypto_pref.CryptoPref
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
    fun provideCryptoPref(@ApplicationContext mContext: Context): CryptoPref {
        return CryptoPref(mContext)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext mContext: Context,
        cryptoPref: CryptoPref
    ): AuthRepository {
        return AuthRepositoryImpl(
            context = mContext,
            auth = Firebase.auth,
            db = Firebase.firestore,
            ioDispatcher = Dispatchers.IO,
            cryptoPref = cryptoPref
        )
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        @ApplicationContext mContext: Context,
        cryptoPref: CryptoPref
    ): ProductRepository {
        return ProductRepositoryImpl(
            context = mContext,
            storage = Firebase.storage.reference,
            firestoreDb = Firebase.firestore,
            ioDispatcher = Dispatchers.IO,
            cryptoPref = cryptoPref
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        @ApplicationContext mContext: Context,
        cryptoPref: CryptoPref
    ): UserRepository {
        return UserRepositoryImpl(
            storage = Firebase.storage,
            db = Firebase.firestore,
            context = mContext,
            ioDispatcher = Dispatchers.IO,
            cryptoPref = cryptoPref
        )
    }

    @Provides
    @Singleton
    fun provideSellerRepository(
        @ApplicationContext mContext: Context,
        cryptoPref: CryptoPref
    ): SellerRepository {
        return SellerRepositoryImpl(
            db = Firebase.firestore,
            storage = Firebase.storage,
            ioDispatcher = Dispatchers.IO,
            context = mContext,
            cryptoPref = cryptoPref
        )
    }

    @Provides
    @Singleton
    fun provideAdminRepository(@ApplicationContext mContext: Context): AdminRepository {
        return AdminRepositoryImpl(
            db = Firebase.firestore,
            mContext = mContext,
            ioDispatcher = Dispatchers.IO
        )
    }
}