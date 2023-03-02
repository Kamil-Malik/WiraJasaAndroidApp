package com.wirajasa.wirajasabisnis.core.ui.detail_service

import androidx.lifecycle.ViewModel
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailServiceViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun getUser(): UserProfile = userRepository.getLocalProfile()
}