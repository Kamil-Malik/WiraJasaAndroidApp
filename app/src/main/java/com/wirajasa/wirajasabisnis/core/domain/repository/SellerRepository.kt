package com.wirajasa.wirajasabisnis.core.domain.repository

import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface SellerRepository {

    fun submitApplication(form: SellerApplication) : Flow<NetworkResponse<Boolean>>

    fun getSellerData(): SellerApplication
}