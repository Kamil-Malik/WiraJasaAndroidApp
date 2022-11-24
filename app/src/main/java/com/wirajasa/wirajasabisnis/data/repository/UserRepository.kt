package com.wirajasa.wirajasabisnis.data.repository

import android.net.Uri
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun uploadProfileImage(uri: Uri, uid: String): Flow<NetworkResponse<Uri>>

    fun registerDefaultProfile() : Flow<NetworkResponse<UserProfile>>

    fun updateProfile(userProfile: UserProfile) : Flow<NetworkResponse<Boolean>>

    fun getUserProfile(): Flow<NetworkResponse<UserProfile>>

    fun getLocalProfile(): UserProfile
}