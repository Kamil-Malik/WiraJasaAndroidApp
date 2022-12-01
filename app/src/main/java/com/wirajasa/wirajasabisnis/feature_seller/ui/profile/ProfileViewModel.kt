package com.wirajasa.wirajasabisnis.feature_seller.ui.profile

import androidx.lifecycle.ViewModel
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.domain.repository.SellerRepository
import com.wirajasa.wirajasabisnis.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sellerRepository: SellerRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun getProfile(): UserProfile {
        return userRepository.getLocalProfile()
    }

    fun getSellerProfile(): SellerApplication {
       return sellerRepository.getSellerData()
    }
}