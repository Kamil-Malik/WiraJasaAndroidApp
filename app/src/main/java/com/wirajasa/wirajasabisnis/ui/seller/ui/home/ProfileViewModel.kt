package com.wirajasa.wirajasabisnis.ui.seller.ui.home

import androidx.lifecycle.ViewModel
import com.wirajasa.wirajasabisnis.data.model.SellerApplication
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.data.repository.SellerRepository
import com.wirajasa.wirajasabisnis.data.repository.UserRepository
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