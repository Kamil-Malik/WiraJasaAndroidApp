package com.wirajasa.wirajasabisnis.buyer.seller_registration

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.domain.repository.SellerRepository
import com.wirajasa.wirajasabisnis.core.domain.repository.UserRepository
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SellerRegistrationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sellerRepository: SellerRepository
): ViewModel() {

    private var imageUri : String = ""

    fun getCurrentUser(): UserProfile =
        userRepository.getLocalProfile()

    fun getImageUri() : String {
        return imageUri
    }

    fun setImageUri(uri: Uri) {
        imageUri = uri.toString()
    }

    fun submitForm(form: SellerApplication) : LiveData<NetworkResponse<Boolean>> {
        form.photoID = imageUri
        return sellerRepository.submitApplication(form).asLiveData()
    }
}