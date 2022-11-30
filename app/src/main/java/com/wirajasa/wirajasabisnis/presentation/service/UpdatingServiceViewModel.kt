package com.wirajasa.wirajasabisnis.presentation.service

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.feature_seller.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class UpdatingServiceViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel(){
    fun updateProduct(
        uid: String,
        serviceId: String,
        name: String,
        price: Int,
        unit: String,
        address: String,
        province: String,
        phoneNumber: String,
        photo: Uri?
    ): LiveData<NetworkResponse<Boolean>> {
        return productRepository.updateProduct(uid, serviceId, name, price, unit, address, province, phoneNumber, photo).flowOn(
            Dispatchers.Main).asLiveData()
    }
}