package com.wirajasa.wirajasabisnis.feature_admin.domain.repository

import com.wirajasa.wirajasabisnis.data.model.SellerApplication
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface AdminRepository {

    fun getApplicationForm() : Flow<NetworkResponse<List<SellerApplication>>>

    fun updateApplicationForm(form: SellerApplication): Flow<NetworkResponse<Boolean>>
}