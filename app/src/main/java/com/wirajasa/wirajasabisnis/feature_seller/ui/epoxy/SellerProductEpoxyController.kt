package com.wirajasa.wirajasabisnis.feature_seller.ui.epoxy

import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.epoxy.controller.GenericServiceEpoxyController
import com.wirajasa.wirajasabisnis.core.epoxy.model.ScreenEmptyEpoxyModel
import com.wirajasa.wirajasabisnis.core.epoxy.model.ScreenErrorEpoxyModel
import com.wirajasa.wirajasabisnis.core.epoxy.model.ScreenLoadingEpoxyModel
import com.wirajasa.wirajasabisnis.core.epoxy.model.ServiceEpoxyModel
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse

class SellerProductEpoxyController(
    onSelected: (ServicePost) -> Unit,
    onRetry: (Boolean) -> Unit
) : GenericServiceEpoxyController(onSelected, onRetry) {

    override fun buildModels() {
        when (data) {
            is NetworkResponse.GenericException -> {
                ScreenErrorEpoxyModel((data as NetworkResponse.GenericException).cause, onRetry).id(
                    "error"
                ).addTo(this)
            }
            is NetworkResponse.Loading -> {
                ScreenLoadingEpoxyModel((data as NetworkResponse.Loading).status).id("loading")
                    .addTo(this)
            }
            is NetworkResponse.Success -> {
                val result = (data as NetworkResponse.Success).data
                if (result.isEmpty()) {
                    ScreenEmptyEpoxyModel("You have not post any service yet").id("emptyScreen").addTo(this)
                } else result.forEach {
                    ServiceEpoxyModel(it, onSelected).id(it.serviceId).addTo(this)
                }
            }
        }
    }
}