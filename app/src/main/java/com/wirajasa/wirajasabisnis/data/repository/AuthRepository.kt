package com.wirajasa.wirajasabisnis.data.repository

import com.google.firebase.auth.FirebaseUser
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher

interface AuthRepository {

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
        dispatcher: CoroutineDispatcher
    ): NetworkResponse<FirebaseUser>

    suspend fun signUpWithEmailAndPassword(email: String, password: String, dispatcher: CoroutineDispatcher) : NetworkResponse<FirebaseUser>

    suspend fun resetPasswordWithEmail(email: String, dispatcher: CoroutineDispatcher) : NetworkResponse<String>

    fun getCurrentUser() : FirebaseUser?
}