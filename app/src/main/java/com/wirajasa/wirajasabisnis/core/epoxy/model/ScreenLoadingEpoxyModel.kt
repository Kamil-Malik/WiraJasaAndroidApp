package com.wirajasa.wirajasabisnis.core.epoxy.model

import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.epoxy.utility.ViewBindingKotlinModel
import com.wirajasa.wirajasabisnis.databinding.LoadingScreenBinding

data class ScreenLoadingEpoxyModel(
    val title: String?
): ViewBindingKotlinModel<LoadingScreenBinding>(R.layout.loading_screen) {

    override fun LoadingScreenBinding.bind() {
        title?.let {
            tvScreenLoadingStatus.text = title
        }
    }
}
