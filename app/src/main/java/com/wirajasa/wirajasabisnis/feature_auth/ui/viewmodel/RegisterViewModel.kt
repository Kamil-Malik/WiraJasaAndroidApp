package com.wirajasa.wirajasabisnis.feature_auth.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.feature_auth.domain.repository.AuthRepository
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): LiveData<NetworkResponse<UserProfile>> {
        return authRepository.signUpWithEmailAndPassword(email, password)
            .asLiveData(Dispatchers.Main)
    }
}