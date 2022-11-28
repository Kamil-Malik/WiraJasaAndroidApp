package com.wirajasa.wirajasabisnis.data.repository

import com.wirajasa.wirajasabisnis.data.model.SellerApplication
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface SellerRepository {

    fun submitApplication(form: SellerApplication) : Flow<NetworkResponse<Boolean>>

    fun getSellerData(): SellerApplication
}