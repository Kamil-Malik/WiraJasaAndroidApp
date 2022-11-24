package com.wirajasa.wirajasabisnis.presentation.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.data.repository.UserRepository
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private var imageUri : String = ""

    fun getUser() : UserProfile {
       return repository.getLocalProfile()
    }

    fun setNewImageUri(uri: Uri) {
        imageUri = uri.toString()
    }

    fun uploadImage(uid: String) : LiveData<NetworkResponse<Uri>> {
        val file = Uri.parse(imageUri)
        return repository.uploadProfileImage(file, uid).asLiveData(Dispatchers.Main)
    }

    fun updateProfile(profile: UserProfile) : LiveData<NetworkResponse<Boolean>> {
        return repository.updateProfile(profile).asLiveData(Dispatchers.Main)
    }
}