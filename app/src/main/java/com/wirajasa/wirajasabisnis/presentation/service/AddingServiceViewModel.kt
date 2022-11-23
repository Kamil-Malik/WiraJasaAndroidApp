package com.wirajasa.wirajasabisnis.presentation.service

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wirajasa.wirajasabisnis.data.repository.ProductRepository
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class AddingServiceViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel(){
    fun addProduct(
        uid: String,
        name: String,
        price:Int,
        unit: String,
        address: String,
        Email: String,
        phoneNumber: String,
        photo: Uri?
    ): LiveData<NetworkResponse<Boolean>>{
        return productRepository.addProduct(uid, name, price, unit, address, Email, phoneNumber, photo).flowOn(Dispatchers.Main).asLiveData()
    }
}