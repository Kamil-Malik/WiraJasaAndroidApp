package com.wirajasa.wirajasabisnis.feature_seller.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.domain.repository.UserRepository
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.feature_seller.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _sellerItem = MutableLiveData<NetworkResponse<List<ServicePost>>>()
    val sellerItem: LiveData<NetworkResponse<List<ServicePost>>> get() = _sellerItem


    fun getAllProductsAccordingUID(uid: String) {
        viewModelScope.launch {
            productRepository.getAllProductsAccordingUID(uid).collect {
                _sellerItem.value = it
            }
        }
    }

    fun getProfile(): UserProfile = userRepo.getLocalProfile()
}