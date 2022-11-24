package com.wirajasa.wirajasabisnis.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseUser
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.data.repository.AuthRepository
import com.wirajasa.wirajasabisnis.data.repository.UserRepository
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun signInWithEmailAndPassword(
        email: String, password: String
    ): LiveData<NetworkResponse<FirebaseUser>> {
        return authRepository.signInWithEmailAndPassword(email, password)
            .asLiveData(Dispatchers.Main)
    }

    fun getProfile(): LiveData<NetworkResponse<UserProfile>> {
        return userRepository.getUserProfile().asLiveData(Dispatchers.Main)
    }

    fun getCurrentUser(): FirebaseUser? = authRepository.getCurrentUser()
}