package com.wirajasa.wirajasabisnis.data.repository

import android.net.Uri
import com.wirajasa.wirajasabisnis.data.model.SellerApplication
import com.wirajasa.wirajasabisnis.data.model.ServicePost
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun addProduct(
        uid: String,
        name: String,
        price:Int,
        unit: String,
        address: String,
        province: String,
        phoneNumber: String,
        photo: Uri?
    ): Flow<NetworkResponse<Boolean>>

    fun getLocalProfile(): SellerApplication

    fun getAllProductsAccordingUID(uid: String): Flow<NetworkResponse<MutableList<ServicePost>>>

//    fun getService() : Flow<NetworkResponse<List<ServicePost>>>
}