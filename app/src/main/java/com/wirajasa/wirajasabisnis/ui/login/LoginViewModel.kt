package com.wirajasa.wirajasabisnis.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.wirajasa.wirajasabisnis.data.repository.AuthRepository
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginStatus = MutableLiveData<NetworkResponse<FirebaseUser>>()
    val loginStatus : LiveData<NetworkResponse<FirebaseUser>> = _loginStatus

    fun signInWithEmailAndPassword(email: String, password: String, dispatcher: CoroutineDispatcher) {
        _loginStatus.value = NetworkResponse.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _loginStatus.value = authRepository
                .signInWithEmailAndPassword(email, password, dispatcher)
        }
    }

    fun getCurrentUser(): FirebaseUser? = authRepository.getCurrentUser()
}