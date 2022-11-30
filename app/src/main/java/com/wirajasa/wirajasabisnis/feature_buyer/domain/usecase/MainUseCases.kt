package com.wirajasa.wirajasabisnis.feature_buyer.domain.usecase

import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.domain.repository.UserRepository
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.feature_buyer.domain.repository.BuyerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainUseCases @Inject constructor(
    private val userRepository: UserRepository,
    private val buyerRepository: BuyerRepository
){

    fun getUser() : UserProfile {
        return userRepository.getLocalProfile()
    }

    fun getAllService(): Flow<NetworkResponse<List<ServicePost>>> {
        return buyerRepository.getAllService()
    }
}