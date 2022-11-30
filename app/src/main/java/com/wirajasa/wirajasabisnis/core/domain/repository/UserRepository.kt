package com.wirajasa.wirajasabisnis.core.domain.repository

import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun updateProfile(userProfile: UserProfile): Flow<NetworkResponse<Boolean>>

    fun getLocalProfile(): UserProfile
}