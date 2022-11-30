package com.wirajasa.wirajasabisnis.feature_buyer.ui.epoxy

import com.airbnb.epoxy.EpoxyController
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.epoxy.ScreenErrorEpoxyModel
import com.wirajasa.wirajasabisnis.core.epoxy.ScreenLoadingEpoxyModel
import com.wirajasa.wirajasabisnis.core.epoxy.ServiceEpoxyModel
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse

class ListOfServiceController(val onSelected: (ServicePost) -> Unit, val onRetry: (Unit) -> Unit) :
    EpoxyController() {

    var data: NetworkResponse<List<ServicePost>> = NetworkResponse.Loading(null)
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        when (data) {
            is NetworkResponse.GenericException -> {
                ScreenErrorEpoxyModel((data as NetworkResponse.GenericException).cause, onRetry = {
                    onRetry
                }).id("error").addTo(this)
            }
            is NetworkResponse.Loading -> {
                ScreenLoadingEpoxyModel((data as NetworkResponse.Loading).status).id("loading").addTo(this)
            }
            is NetworkResponse.Success -> {
                (data as NetworkResponse.Success).data.forEach {
                    ServiceEpoxyModel(it, onSelected).id(it.serviceId).addTo(this)
                }
            }
        }
    }
}