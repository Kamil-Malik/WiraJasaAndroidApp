package com.wirajasa.wirajasabisnis.feature_buyer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.feature_buyer.domain.usecase.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: MainUseCases
) : ViewModel() {

    private val _listOfService: MutableLiveData<NetworkResponse<List<ServicePost>>> =
        MutableLiveData()
    val listOfService: LiveData<NetworkResponse<List<ServicePost>>> = _listOfService

    fun getUser(): UserProfile {
        return useCases.getUser()
    }

    fun getAllService() {
        viewModelScope.launch {
            useCases.getAllService().collect {
                _listOfService.value = it
            }
        }
    }

    fun getServiceByName() {

    }

    fun getServiceByProvince() {

    }
}