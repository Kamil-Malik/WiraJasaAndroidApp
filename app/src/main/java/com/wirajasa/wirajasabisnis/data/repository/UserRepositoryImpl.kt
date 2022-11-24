package com.wirajasa.wirajasabisnis.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.data.local.CryptoPref
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.usecases.HandleException
import com.wirajasa.wirajasabisnis.utility.Constant
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.utility.constant.FirebaseCollection.USER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UserRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val fireStore: FirebaseFirestore,
    private val authRepository: AuthRepository,
    private val context: Context,
    private val ioDispatcher: CoroutineContext,
    private val cryptoPref: CryptoPref
) : UserRepository {

    override fun uploadProfileImage(uri: Uri, uid: String): Flow<NetworkResponse<Uri>> = flow {
        try {
            val task = storage.reference.child("$USER/$uid.png").putFile(uri).await()
            emit(NetworkResponse.Success(task.storage.downloadUrl.await()))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(HandleException().getMessage(e, context)))
        }
    }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun registerDefaultProfile(): Flow<NetworkResponse<UserProfile>> = flow {
        try {
            val currentUser = authRepository.getCurrentUser() as FirebaseUser
            val notSetup = context.getString(R.string.not_setup)
            val profile = UserProfile(
                uid = currentUser.uid,
                username = "Guest${UUID.randomUUID()}",
                address = notSetup,
                phone_number = notSetup,
                image = "",
                sellerStatus = false,
                createdAt = Timestamp.now().toDate().toString()
            )
            fireStore.collection(Constant.COLLECTION_USER).document(currentUser.uid).set(profile)
                .await()
            saveProfile(profile)
            emit(NetworkResponse.Success(profile))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(HandleException().getMessage(e, context)))
        }
    }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun updateProfileWithNewImage(userProfile: UserProfile): Flow<NetworkResponse<Boolean>> =
        flow {
            try {
                val newImageUri = storage.reference
                    .child("$USER/${userProfile.uid}.png")
                    .putFile(Uri.parse(userProfile.image))
                    .await()
                    .storage.downloadUrl
                    .await()

                emit(NetworkResponse.Loading)

                userProfile.image = newImageUri.toString()

                fireStore.collection(USER).document(userProfile.uid)
                    .set(userProfile, SetOptions.merge()).await()

                emit(NetworkResponse.Success(true))
            } catch (e: Exception) {
                val message = HandleException().getMessage(e, context)
                emit(NetworkResponse.GenericException(message))
            }
        }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun updateProfileWithoutNewImage(userProfile: UserProfile): Flow<NetworkResponse<Boolean>> =
        flow {
            try {
                saveProfile(userProfile)
                fireStore.collection(USER).document(userProfile.uid)
                    .set(userProfile, SetOptions.merge()).await()
                emit(NetworkResponse.Success(true))
            } catch (e: Exception) {
                emit(NetworkResponse.GenericException(HandleException().getMessage(e, context)))
            }
        }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun getUserProfile(): Flow<NetworkResponse<UserProfile>> = flow {
        try {
            val uid = authRepository.getCurrentUser()?.uid as String
            val result = fireStore.collection(USER).document(uid).get().await()
                .toObject(UserProfile::class.java) as UserProfile
            saveProfile(result)
            emit(NetworkResponse.Success(result))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(HandleException().getMessage(e, context)))
        }
    }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun getLocalProfile(): UserProfile {
        return cryptoPref.getProfile()
    }

    private fun saveProfile(profile: UserProfile) {
        cryptoPref.saveProfile(profile)
    }
}