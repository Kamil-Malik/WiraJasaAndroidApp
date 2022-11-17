package com.wirajasa.wirajasabisnis.presentation.register_buyyer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseUser
import com.wirajasa.wirajasabisnis.data.repository.AuthRepository
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): LiveData<NetworkResponse<FirebaseUser>> {
        return authRepository.signUpWithEmailAndPassword(email, password).flowOn(Dispatchers.Main)
            .asLiveData()
    }
}