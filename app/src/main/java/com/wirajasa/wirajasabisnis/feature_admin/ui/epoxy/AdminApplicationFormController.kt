package com.wirajasa.wirajasabisnis.feature_admin.ui.epoxy

import com.airbnb.epoxy.EpoxyController
import com.wirajasa.wirajasabisnis.core.epoxy.model.ScreenErrorEpoxyModel
import com.wirajasa.wirajasabisnis.core.epoxy.model.ScreenLoadingEpoxyModel
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.utility.constant.EpoxyId.EPOXY_ERROR
import com.wirajasa.wirajasabisnis.utility.constant.EpoxyId.EPOXY_LOADING

class AdminApplicationFormController(
    val onSelected: (SellerApplication) -> Unit, private val onRetry: (Boolean) -> Unit
) : EpoxyController() {

    var status: NetworkResponse<List<SellerApplication>> = NetworkResponse.Loading(null)
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        when (status) {
            is NetworkResponse.GenericException -> ScreenErrorEpoxyModel(
                (status as NetworkResponse.GenericException).cause
            ) {
                onRetry(it)
            }.id(EPOXY_ERROR).addTo(this)
            is NetworkResponse.Loading ->
                ScreenLoadingEpoxyModel((status as NetworkResponse.Loading).status)
                    .id(EPOXY_LOADING)
                    .addTo(this)
            is NetworkResponse.Success -> {
                val data = (status as NetworkResponse.Success).data
                data.forEach { sellerApplication ->
                    ApplicationFormEpoxyModel(sellerApplication, onClicked = {
                        onSelected(it)
                    }).id(sellerApplication.applicationId).addTo(this)
                }
            }
        }
    }
}