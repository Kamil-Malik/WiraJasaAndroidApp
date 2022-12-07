package com.wirajasa.wirajasabisnis.feature_admin.domain.repository

import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface AdminRepository {

    fun getApplicationForm() : Flow<NetworkResponse<List<SellerApplication>>>

    fun updateApplicationForm(form: SellerApplication): Flow<NetworkResponse<Boolean>>
}