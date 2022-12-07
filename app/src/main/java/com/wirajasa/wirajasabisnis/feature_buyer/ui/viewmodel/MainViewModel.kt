package com.wirajasa.wirajasabisnis.feature_buyer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.domain.repository.UserRepository
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.feature_buyer.domain.repository.BuyerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val buyerRepository: BuyerRepository
) : ViewModel() {

    private val _listOfService: MutableLiveData<NetworkResponse<List<ServicePost>>> =
        MutableLiveData()
    val listOfService: LiveData<NetworkResponse<List<ServicePost>>> = _listOfService

    fun getUser(): UserProfile {
        return userRepository.getLocalProfile()
    }

    fun getAllService() {
        viewModelScope.launch {
            buyerRepository.getAllService().collect {
                _listOfService.value = it
            }
        }
    }

    fun getServiceByName(provinceName: String) {
        viewModelScope.launch {
            buyerRepository.getAllServiceByProvince(provinceName).collect {
                _listOfService.value = it
            }
        }
    }
}