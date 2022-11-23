package com.wirajasa.wirajasabisnis.presentation.profile

import androidx.lifecycle.ViewModel
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun getUser() : UserProfile {
       return authRepository.getLocalProfile()
    }
}