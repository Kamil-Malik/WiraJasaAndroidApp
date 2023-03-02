package com.wirajasa.wirajasabisnis.role_buyer.edit_profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.domain.repository.UserRepository
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var imageUri: String = ""

    fun setNewImageUri(uri: Uri) {
        imageUri = uri.toString()
    }

    fun getProfile() = userRepository.getLocalProfile()

    fun updateProfile(profile: UserProfile): LiveData<NetworkResponse<Boolean>> {
        if(imageUri.isNotBlank()) profile.image = imageUri
        return userRepository.updateProfile(profile).asLiveData(Dispatchers.Main)
    }
}