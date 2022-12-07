package com.wirajasa.wirajasabisnis.feature_admin.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.feature_admin.domain.repository.AdminRepository
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DetailFormViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    fun updateForm(form: SellerApplication): LiveData<NetworkResponse<Boolean>> {
        return repository.updateApplicationForm(form).asLiveData(Dispatchers.Main)
    }
}