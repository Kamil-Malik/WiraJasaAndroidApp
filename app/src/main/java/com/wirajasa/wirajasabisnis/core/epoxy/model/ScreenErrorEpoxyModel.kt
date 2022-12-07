package com.wirajasa.wirajasabisnis.core.epoxy.model

import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.epoxy.utility.ViewBindingKotlinModel
import com.wirajasa.wirajasabisnis.databinding.ScreenErrorBinding

data class ScreenErrorEpoxyModel(
    val error: String?,
    val onRetry: (Boolean)-> Unit
) : ViewBindingKotlinModel<ScreenErrorBinding>(R.layout.screen_error) {

    override fun ScreenErrorBinding.bind() {
        error?.let { tvError.text = it }
        btnRetryTask.setOnClickListener {
            onRetry(true)
        }
    }
}
