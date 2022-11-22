package com.wirajasa.wirajasabisnis.data.repository


import android.content.Context
import androidx.core.content.edit
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val ioDispatcher: CoroutineContext
) : AuthRepository {

    override fun signInWithEmailAndPassword(
        email: String, password: String
    ): Flow<NetworkResponse<FirebaseUser>> = flow {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            val currentUser = getCurrentUser() as FirebaseUser
            emit(NetworkResponse.Success(data = currentUser))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(getExceptionMessage(e)))
        }
    }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun signUpWithEmailAndPassword(
        email: String, password: String
    ): Flow<NetworkResponse<Boolean>> = flow {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            emit(NetworkResponse.Success(false))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(getExceptionMessage(e)))
        }
    }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun registerDefaultProfile(): Flow<NetworkResponse<UserProfile>> =
        flow {
            try {
                val currentUser = getCurrentUser() as FirebaseUser
                val profile = UserProfile(
                    uid = currentUser.uid,
                    username = "Guest${UUID.randomUUID()}",
                    address = "Not Setup",
                    phone_number = "Not Setup",
                    seller = false
                )
                fireStore.collection(COLLECTION_USER).document(currentUser.uid).set(profile).await()
                context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE).edit {
                    putString(UID, profile.uid)
                    putString(USERNAME, profile.username)
                    putString(ADDRESS, profile.address)
                    putString(PHONE_NUMBER, profile.phone_number)
                    putBoolean(SELLER, profile.seller)
                    apply()
                }
                emit(NetworkResponse.Success(profile))
            } catch (e: Exception) {
                emit(NetworkResponse.GenericException(getExceptionMessage(e)))
            }
        }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun resetPasswordWithEmail(
        email: String
    ): Flow<NetworkResponse<Boolean>> = flow {
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(NetworkResponse.Success(data = true))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(getExceptionMessage(e)))
        }
    }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    private fun getExceptionMessage(exception: Exception): String {
        return when (exception) {
            is IOException -> context.getString(R.string.connection_error)
            is FirebaseAuthEmailException -> context.getString(R.string.invalid_email)
            is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.invalid_credential)
            is FirebaseAuthInvalidUserException -> context.getString(R.string.invalid_account)
            else -> context.getString(R.string.something_went_wrong)
        }
    }

    companion object {
        private const val COLLECTION_USER = "user"
        private const val UID = "uid"
        private const val USERNAME = "username"
        private const val PHONE_NUMBER = "phone_number"
        private const val ADDRESS = "address"
        private const val SELLER = "seller"
        private const val USER_PREF = "user_pref"
    }
}