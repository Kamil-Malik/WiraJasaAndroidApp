package com.wirajasa.wirajasabisnis.data.repository


import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.data.local.CryptoPref
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.usecases.HandleException
import com.wirajasa.wirajasabisnis.utility.Constant.COLLECTION_USER
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val ioDispatcher: CoroutineContext,
    private val cryptoPref: CryptoPref
) : AuthRepository {

    override fun signInWithEmailAndPassword(
        email: String, password: String
    ): Flow<NetworkResponse<UserProfile>> = flow {
        try {
            auth.signInWithEmailAndPassword(email, password).await()

            emit(NetworkResponse.Loading(context.getString(R.string.getting_profile)))
            val userProfile =
                fireStore.collection(COLLECTION_USER).document(auth.currentUser?.uid as String)
                    .get().await().toObject(UserProfile::class.java) as UserProfile

            saveProfile(userProfile)
            emit(NetworkResponse.Success(userProfile))
        } catch (e: Exception) {
            emit(
                NetworkResponse.GenericException(
                    HandleException(context).getMessage(e)
                )
            )
        }
    }.onStart { emit(NetworkResponse.Loading(context.getString(R.string.signing_in))) }
        .flowOn(ioDispatcher)

    override fun signUpWithEmailAndPassword(
        email: String, password: String
    ): Flow<NetworkResponse<UserProfile>> = flow {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()

            emit(NetworkResponse.Loading(context.getString(R.string.uploading_profile)))
            val defaultProfile = getDefaultProfile()
            fireStore.collection(COLLECTION_USER).document((getCurrentUser() as FirebaseUser).uid)
                .set(defaultProfile, SetOptions.merge()).await()

            emit(NetworkResponse.Success(defaultProfile))
        } catch (e: Exception) {
            emit(
                NetworkResponse.GenericException(
                    HandleException(context).getMessage(e)
                )
            )
        }
    }.onStart { emit(NetworkResponse.Loading(context.getString(R.string.signing_up))) }
        .flowOn(ioDispatcher)

    override fun resetPasswordWithEmail(
        email: String
    ): Flow<NetworkResponse<Boolean>> = flow {
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(NetworkResponse.Success(true))
        } catch (e: Exception) {
            emit(
                NetworkResponse.GenericException(
                    HandleException(context).getMessage(e)
                )
            )
        }
    }.onStart { emit(NetworkResponse.Loading(null)) }
        .flowOn(ioDispatcher)

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    private fun getDefaultProfile(): UserProfile {
        val notSetup = context.getString(R.string.not_setup)
        return UserProfile(
            uid = (auth.currentUser as FirebaseUser).uid,
            username = "Guest${UUID.randomUUID()}",
            address = notSetup,
            phone_number = notSetup,
            sellerStatus = false,
        )
    }

    private fun saveProfile(userProfile: UserProfile) {
        cryptoPref.saveProfile(userProfile)
    }
}