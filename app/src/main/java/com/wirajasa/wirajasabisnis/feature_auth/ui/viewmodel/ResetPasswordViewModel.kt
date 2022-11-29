package com.wirajasa.wirajasabisnis.feature_auth.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wirajasa.wirajasabisnis.feature_auth.domain.repository.AuthRepository
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun resetPasswordWithEmailAddress(email: String): LiveData<NetworkResponse<Boolean>> {
        return authRepository.resetPasswordWithEmail(email)
            .asLiveData(Dispatchers.Main)
    }
}