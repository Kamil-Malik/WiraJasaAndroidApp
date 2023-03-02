package com.wirajasa.wirajasabisnis.role_admin.detail_form

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
class DetailFormViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    private val _formStatus: MutableLiveData<NetworkResponse<Boolean>> = MutableLiveData()
    val formStatus: LiveData<NetworkResponse<Boolean>> get() = _formStatus

    fun updateForm(form: SellerApplication) = viewModelScope.launch {
        repository.updateApplicationForm(form).collect {
            _formStatus.value = it
        }
    }
}