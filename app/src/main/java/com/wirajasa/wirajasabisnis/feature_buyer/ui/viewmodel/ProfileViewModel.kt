package com.wirajasa.wirajasabisnis.feature_buyer.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.domain.repository.UserRepository
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