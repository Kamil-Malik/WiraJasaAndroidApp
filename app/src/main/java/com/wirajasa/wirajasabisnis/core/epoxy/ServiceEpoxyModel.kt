package com.wirajasa.wirajasabisnis.core.epoxy

import com.bumptech.glide.Glide
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.databinding.ItemListDashboardBinding

data class ServiceEpoxyModel(
    val data: ServicePost,
    val onSelected: (ServicePost) -> Unit
) : ViewBindingKotlinModel<ItemListDashboardBinding>(R.layout.item_list_dashboard) {

    override fun ItemListDashboardBinding.bind() {
        Glide.with(root.context)
            .load(data.photoUrl)
            .into(ivItemPhoto)

        tvTitle.text = data.name
        tvPrice.text = data.price.toString()
        root.setOnClickListener {
            onSelected(data)
        }
    }
}