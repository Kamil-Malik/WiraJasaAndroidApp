package com.wirajasa.wirajasabisnis.data.repository

import com.google.firebase.auth.FirebaseUser
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun signInWithEmailAndPassword(
        email: String, password: String
    ): Flow<NetworkResponse<FirebaseUser>>

    fun signUpWithEmailAndPassword(
        email: String, password: String
    ): Flow<NetworkResponse<Boolean>>

    fun resetPasswordWithEmail(
        email: String
    ): Flow<NetworkResponse<Boolean>>

    fun getCurrentUser(): FirebaseUser?
}