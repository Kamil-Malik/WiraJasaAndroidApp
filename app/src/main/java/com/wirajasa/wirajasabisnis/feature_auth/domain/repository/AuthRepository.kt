package com.wirajasa.wirajasabisnis.feature_auth.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun signInWithEmailAndPassword(
        email: String, password: String
    ): Flow<NetworkResponse<UserProfile>>

    fun signUpWithEmailAndPassword(
        email: String, password: String
    ): Flow<NetworkResponse<UserProfile>>

    fun resetPasswordWithEmail(
        email: String
    ): Flow<NetworkResponse<Boolean>>

    fun getCurrentUser(): FirebaseUser?
}