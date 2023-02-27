package com.wirajasa.wirajasabisnis.core.epoxy.controller

import com.airbnb.epoxy.EpoxyController
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse

open class GenericServiceEpoxyController(
    val onSelected: (ServicePost) -> Unit,
    val onRetry: () -> Unit
) : EpoxyController() {

    var data: NetworkResponse<List<ServicePost>> = NetworkResponse.Loading(null)
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        return
    }
}