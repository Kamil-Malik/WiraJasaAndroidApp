package com.wirajasa.wirajasabisnis.feature_seller.domain.repository

import android.net.Uri
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun addProduct(
        uid: String,
        name: String,
        price: Int,
        unit: String,
        address: String,
        province: String,
        phoneNumber: String,
        photo: Uri?
    ): Flow<NetworkResponse<Boolean>>

    fun getLocalProfile(): SellerApplication

    fun getAllProductsAccordingUID(uid: String): Flow<NetworkResponse<List<ServicePost>>>

    fun updateProduct(
        uid: String,
        serviceId: String,
        name: String,
        price: Int,
        unit: String,
        address: String,
        province: String,
        phoneNumber: String,
        photo: Uri?,
        photoUrl: String
    ): Flow<NetworkResponse<Boolean>>

//    fun getService() : Flow<NetworkResponse<List<ServicePost>>>
}