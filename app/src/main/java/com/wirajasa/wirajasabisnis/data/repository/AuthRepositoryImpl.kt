package com.wirajasa.wirajasabisnis.data.repository


import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.usecases.HandleException
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val auth: FirebaseAuth,
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
            emit(NetworkResponse.GenericException(HandleException().getMessage(e, context)))
        }
    }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun signUpWithEmailAndPassword(
        email: String, password: String
    ): Flow<NetworkResponse<Boolean>> = flow {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            emit(NetworkResponse.Success(false))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(HandleException().getMessage(e, context)))
        }
    }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun resetPasswordWithEmail(
        email: String
    ): Flow<NetworkResponse<Boolean>> = flow {
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(NetworkResponse.Success(data = true))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(HandleException().getMessage(e, context)))
        }
    }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }
}