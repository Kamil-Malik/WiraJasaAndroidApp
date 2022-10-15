package com.wirajasa.wirajasabisnis.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
        dispatcher: CoroutineDispatcher
    ): NetworkResponse<FirebaseUser> {
        return withContext(dispatcher) {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                val currentUser = getCurrentUser() as FirebaseUser
                NetworkResponse.Success(data = currentUser)
            } catch (e: Exception) {
                when (e) {
                    is IOException -> NetworkResponse.NetworkException
                    else -> NetworkResponse.FirebaseException(e = e)
                }
            }
        }
    }

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        dispatcher: CoroutineDispatcher
    ): NetworkResponse<FirebaseUser> {
        return withContext(dispatcher) {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                val currentUser = getCurrentUser() as FirebaseUser
                NetworkResponse.Success(data = currentUser)
            } catch (e: Exception) {
                when (e) {
                    is IOException -> NetworkResponse.NetworkException
                    else -> NetworkResponse.FirebaseException(e)
                }
            }
        }
    }

    override suspend fun resetPasswordWithEmail(
        email: String,
        dispatcher: CoroutineDispatcher
    ): NetworkResponse<String> {
        return withContext(dispatcher) {
            try {
                auth.sendPasswordResetEmail(email).await()
                NetworkResponse.Success(data = "Sukses")
            } catch (e: Exception) {
                when (e) {
                    is IOException -> NetworkResponse.NetworkException
                    else -> NetworkResponse.FirebaseException(e)
                }
            }
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }
}