package com.wirajasa.wirajasabisnis.data.repository

import android.net.Uri
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun addProduct(
        uid: String,
        name: String,
        price:Int,
        unit: String,
        address: String,
        Email: String,
        phoneNumber: String,
        photo: Uri?
    ): Flow<NetworkResponse<Boolean>>

//    fun getService() : Flow<NetworkResponse<List<ServicePost>>>
}