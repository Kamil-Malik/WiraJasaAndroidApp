package com.wirajasa.wirajasabisnis.ui.reset_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirajasa.wirajasabisnis.data.repository.AuthRepository
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _resetPasswordStatus = MutableLiveData<NetworkResponse<String>>()
    val resetPasswordStatus: LiveData<NetworkResponse<String>> get() = _resetPasswordStatus

    fun sendResetPasswordLink(email: String, dispatcher: CoroutineDispatcher) {
        viewModelScope.launch {
            _resetPasswordStatus.value = NetworkResponse.Loading
            _resetPasswordStatus.value = authRepository.resetPasswordWithEmail(email, dispatcher)
        }
    }
}