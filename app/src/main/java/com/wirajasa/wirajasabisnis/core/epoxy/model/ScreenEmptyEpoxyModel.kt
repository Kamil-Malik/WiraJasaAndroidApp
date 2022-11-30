package com.wirajasa.wirajasabisnis.core.epoxy.model

import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.epoxy.utility.ViewBindingKotlinModel
import com.wirajasa.wirajasabisnis.databinding.ScreenEmptyBinding

data class ScreenEmptyEpoxyModel(val message: String): ViewBindingKotlinModel<ScreenEmptyBinding>(
    R.layout.screen_empty) {
    override fun ScreenEmptyBinding.bind() {
        tvHolderText.text = message
    }
}
