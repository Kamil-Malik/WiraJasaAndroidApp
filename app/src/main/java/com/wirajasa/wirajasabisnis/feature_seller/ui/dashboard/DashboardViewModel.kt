package com.wirajasa.wirajasabisnis.feature_seller.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.feature_seller.domain.repository.ProductRepository
import com.wirajasa.wirajasabisnis.core.domain.repository.UserRepository
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
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
            LiveData<NetworkResponse<List<ServicePost>>> {
        return productRepository.getAllProductsAccordingUID(uid)
            .flowOn(Dispatchers.Main).asLiveData()
    }

    fun getProfile(): UserProfile = userRepo.getLocalProfile()
}