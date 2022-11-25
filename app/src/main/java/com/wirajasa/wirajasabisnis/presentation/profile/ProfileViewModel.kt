package com.wirajasa.wirajasabisnis.presentation.profile

import androidx.lifecycle.ViewModel
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    fun getUser() : UserProfile {
       return repository.getLocalProfile()
    }
}