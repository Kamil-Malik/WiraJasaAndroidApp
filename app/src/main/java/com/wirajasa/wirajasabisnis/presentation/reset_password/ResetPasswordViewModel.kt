package com.wirajasa.wirajasabisnis.presentation.reset_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wirajasa.wirajasabisnis.data.repository.AuthRepository
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
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