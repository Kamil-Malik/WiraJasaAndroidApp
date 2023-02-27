package com.wirajasa.wirajasabisnis.feature_seller.ui.epoxy

import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.epoxy.controller.GenericServiceEpoxyController
import com.wirajasa.wirajasabisnis.core.epoxy.model.ScreenEmptyEpoxyModel
import com.wirajasa.wirajasabisnis.core.epoxy.model.ScreenErrorEpoxyModel
import com.wirajasa.wirajasabisnis.core.epoxy.model.ScreenLoadingEpoxyModel
import com.wirajasa.wirajasabisnis.core.epoxy.model.ServiceEpoxyModel
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.core.utility.constant.EpoxyUtil.EPOXY_EMPTY
import com.wirajasa.wirajasabisnis.core.utility.constant.EpoxyUtil.EPOXY_EMPTY_MESSAGE
import com.wirajasa.wirajasabisnis.core.utility.constant.EpoxyUtil.EPOXY_ERROR
import com.wirajasa.wirajasabisnis.core.utility.constant.EpoxyUtil.EPOXY_LOADING

class SellerProductEpoxyController(
    onSelected: (ServicePost) -> Unit,
    onRetry: () -> Unit
) : GenericServiceEpoxyController(onSelected, onRetry) {

    override fun buildModels() {
        when (data) {
            is NetworkResponse.GenericException -> {
                ScreenErrorEpoxyModel((data as NetworkResponse.GenericException).cause) {
                    onRetry.invoke()
                }.id(EPOXY_ERROR)
                    .addTo(this)
            }

            is NetworkResponse.Loading -> {
                ScreenLoadingEpoxyModel((data as NetworkResponse.Loading).status)
                    .id(EPOXY_LOADING)
                    .addTo(this)
            }

            is NetworkResponse.Success -> {
                val result = (data as NetworkResponse.Success).data
                if (result.isEmpty()) {
                    ScreenEmptyEpoxyModel(EPOXY_EMPTY_MESSAGE)
                        .id(EPOXY_EMPTY)
                        .addTo(this)
                } else result.forEach {
                    ServiceEpoxyModel(it, onSelected).id(it.serviceId).addTo(this)
                }
            }
        }
    }
}