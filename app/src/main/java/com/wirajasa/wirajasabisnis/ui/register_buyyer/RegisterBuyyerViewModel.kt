package com.wirajasa.wirajasabisnis.ui.register_buyyer

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.wirajasa.wirajasabisnis.data.repository.AuthRepository
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterBuyyerViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerStatus = MutableLiveData<NetworkResponse<FirebaseUser>>()
    val registerStatus: LiveData<NetworkResponse<FirebaseUser>> get() = _registerStatus

    private val _emailError = MutableStateFlow("")
    val emailError: StateFlow<String> = _emailError.asStateFlow()

    fun signUpUserWithEmailAndPassword(
        email: String,
        password: String,
        dispatcher: CoroutineDispatcher
    ) {
        viewModelScope.launch {

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _emailError.emit("Invalid Email")
                return@launch
            }

            _registerStatus.value = NetworkResponse.Loading
            _registerStatus.value =
                authRepository.signUpWithEmailAndPassword(email, password, dispatcher)
        }
    }

    fun getCurrentUser(): FirebaseUser? = authRepository.getCurrentUser()
}