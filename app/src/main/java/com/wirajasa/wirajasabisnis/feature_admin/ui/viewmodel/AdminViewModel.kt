package com.wirajasa.wirajasabisnis.feature_admin.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wirajasa.wirajasabisnis.data.model.SellerApplication
import com.wirajasa.wirajasabisnis.feature_admin.domain.repository.AdminRepository
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    fun getApplicationList(): LiveData<NetworkResponse<List<SellerApplication>>> {
        return adminRepository.getApplicationForm().asLiveData(Dispatchers.Main)
    }
}