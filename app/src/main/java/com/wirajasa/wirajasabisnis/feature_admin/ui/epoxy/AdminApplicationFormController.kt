package com.wirajasa.wirajasabisnis.feature_admin.ui.epoxy

import com.airbnb.epoxy.EpoxyController
import com.wirajasa.wirajasabisnis.core.epoxy.ScreenErrorEpoxyModel
import com.wirajasa.wirajasabisnis.core.epoxy.ScreenLoadingEpoxyModel
import com.wirajasa.wirajasabisnis.data.model.SellerApplication
import com.wirajasa.wirajasabisnis.utility.NetworkResponse

class AdminApplicationFormController(
    val onSelected: (SellerApplication) -> Unit,
    private val onRetry: () -> Unit
) : EpoxyController() {

    var status: NetworkResponse<List<SellerApplication>> = NetworkResponse.Loading(null)
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        when (status) {
            is NetworkResponse.GenericException -> ScreenErrorEpoxyModel(
                (status as NetworkResponse.GenericException).cause, onRetry
            ).id("error").addTo(this)
            is NetworkResponse.Loading -> ScreenLoadingEpoxyModel((status as NetworkResponse.Loading).status)
                .id("loading")
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