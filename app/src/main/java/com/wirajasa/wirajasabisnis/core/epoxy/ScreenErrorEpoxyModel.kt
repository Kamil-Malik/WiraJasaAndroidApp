package com.wirajasa.wirajasabisnis.core.epoxy

import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ScreenErrorBinding

data class ScreenErrorEpoxyModel(
    val error: String?,
    val onRetry: ()-> Unit
) :ViewBindingKotlinModel<ScreenErrorBinding>(R.layout.screen_error) {

    override fun ScreenErrorBinding.bind() {
        error?.let { tvError.text = it }
        btnRetry.setOnClickListener { onRetry }
    }
}
