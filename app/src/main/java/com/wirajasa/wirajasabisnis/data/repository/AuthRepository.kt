package com.wirajasa.wirajasabisnis.data.repository

interface AuthRepository {

    suspend fun signInWithEmailAndPassword(email: String, password: String)

    suspend fun signUpWithEmailAndPassword(email: String, password: String)

    suspend fun resetPasswordWithEmail(email: String)
}