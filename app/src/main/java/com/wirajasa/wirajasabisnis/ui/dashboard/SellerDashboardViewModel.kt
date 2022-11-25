package com.wirajasa.wirajasabisnis.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirajasa.wirajasabisnis.data.model.Product
import kotlinx.coroutines.launch

enum class SellerProductApiStatus {LOADING, SUCCESS, ERROR}

class SellerDashboardViewModel: ViewModel() {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> = _product

    private val _status = MutableLiveData<SellerProductApiStatus>()
    val status: LiveData<SellerProductApiStatus> = _status

    init {
        getProductList()
    }

    fun getProductList() {

        viewModelScope.launch {
            _status.value = SellerProductApiStatus.LOADING
//            try {
//                _product.value =
//                _status.value = ProductApiStatus.SUCCESS
//            } catch (e: Exception) {
//                _product.value = listOf()
//                _status.value = ProductApiStatus.ERROR
//                Log.e("ErrorMsg", "${e.message}")
//            }
        }
    }
}

