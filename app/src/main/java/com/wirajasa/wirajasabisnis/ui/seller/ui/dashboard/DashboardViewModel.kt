package com.wirajasa.wirajasabisnis.ui.seller.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wirajasa.wirajasabisnis.data.model.ServicePost
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.data.repository.ProductRepository
import com.wirajasa.wirajasabisnis.data.repository.UserRepository
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepo : UserRepository
) : ViewModel() {
    fun getAllProductsAccordingUID(uid: String):
            LiveData<NetworkResponse<MutableList<ServicePost>>>{
        return productRepository.getAllProductsAccordingUID(uid)
            .flowOn(Dispatchers.Main).asLiveData()
    }

    fun getProfile(): UserProfile = userRepo.getLocalProfile()
}