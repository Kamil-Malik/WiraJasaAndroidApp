package com.wirajasa.wirajasabisnis.core.epoxy.model

import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.epoxy.utility.ViewBindingKotlinModel
import com.wirajasa.wirajasabisnis.databinding.ErrorScreenBinding

data class ScreenErrorEpoxyModel(
    val error: String?,
    val onRetry: () -> Unit
) : ViewBindingKotlinModel<ErrorScreenBinding>(R.layout.error_screen) {

    override fun ErrorScreenBinding.bind() {
        error?.let { tvError.text = it }
        btnRetryTask.setOnClickListener {
            onRetry.invoke()
        }
    }
}
