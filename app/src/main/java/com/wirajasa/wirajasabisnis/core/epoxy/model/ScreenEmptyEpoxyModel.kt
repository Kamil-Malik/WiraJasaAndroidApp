package com.wirajasa.wirajasabisnis.core.epoxy.model

import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.epoxy.utility.ViewBindingKotlinModel
import com.wirajasa.wirajasabisnis.databinding.EmptyScreenBinding

data class ScreenEmptyEpoxyModel(val message: String): ViewBindingKotlinModel<EmptyScreenBinding>(
    R.layout.empty_screen) {
    override fun EmptyScreenBinding.bind() {
        tvHolderText.text = message
    }
}
