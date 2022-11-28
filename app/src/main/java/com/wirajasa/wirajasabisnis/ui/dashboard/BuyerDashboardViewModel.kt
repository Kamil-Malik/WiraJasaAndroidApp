package com.wirajasa.wirajasabisnis.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirajasa.wirajasabisnis.data.model.Product
import kotlinx.coroutines.launch

enum class BuyerProductApiStatus {LOADING, SUCCESS, ERROR}

class BuyerDashboardViewModel: ViewModel() {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> = _product

    private val _status = MutableLiveData<BuyerProductApiStatus>()
    val status: LiveData<BuyerProductApiStatus> = _status

    init {
        getProductList()
    }

    fun getProductList() {

        viewModelScope.launch {
            _status.value = BuyerProductApiStatus.LOADING
//            try {
//                _product.value =
//                _status.value = BuyerProductApiStatus.SUCCESS
//            } catch (e: Exception) {
//                _product.value = listOf()
//                _status.value = ProductApiStatus.ERROR
//                Log.e("ErrorMsg", "${e.message}")
//            }
        }
    }

}