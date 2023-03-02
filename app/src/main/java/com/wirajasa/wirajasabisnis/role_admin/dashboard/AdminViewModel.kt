package com.wirajasa.wirajasabisnis.role_admin.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.feature_admin.domain.repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _applicationList = MutableLiveData<NetworkResponse<List<SellerApplication>>>()
    val applicationList: LiveData<NetworkResponse<List<SellerApplication>>> get() = _applicationList

    init {
        Log.d("VM Admin", "VM Created")
        getApplicationList()
    }

    fun getApplicationList() {
        viewModelScope.launch {
            adminRepository.getApplicationForm().collect {
                _applicationList.value = it
            }
        }
    }
}