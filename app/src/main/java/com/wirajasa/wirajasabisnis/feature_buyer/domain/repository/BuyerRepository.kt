package com.wirajasa.wirajasabisnis.feature_buyer.domain.repository

import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface BuyerRepository {

    fun getAllService(): Flow<NetworkResponse<List<ServicePost>>>

    fun getAllServiceByProvince(provinceName: String): Flow<NetworkResponse<List<ServicePost>>>
}