package com.wirajasa.wirajasabisnis.data.repository

import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun updateProfile(userProfile: UserProfile): Flow<NetworkResponse<Boolean>>

    fun getLocalProfile(): UserProfile
}