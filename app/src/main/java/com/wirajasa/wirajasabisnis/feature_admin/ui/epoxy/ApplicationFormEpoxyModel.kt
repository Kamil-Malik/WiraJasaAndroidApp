package com.wirajasa.wirajasabisnis.feature_admin.ui.epoxy

import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.epoxy.ViewBindingKotlinModel
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.databinding.ItemRowApplicationFormBinding
import com.wirajasa.wirajasabisnis.feature_admin.domain.repository_impl.AdminRepositoryImpl

data class ApplicationFormEpoxyModel(
    val applicationForm: SellerApplication,
    val onClicked: (SellerApplication) -> Unit
) : ViewBindingKotlinModel<ItemRowApplicationFormBinding>(R.layout.item_row_application_form) {

    override fun ItemRowApplicationFormBinding.bind() {
        when(applicationForm.applicationStatus) {
            AdminRepositoryImpl.PENDING -> root.background.setTint(root.context.getColor(R.color.pending))
            AdminRepositoryImpl.APPROVED -> root.background.setTint(root.context.getColor(R.color.approved))
            AdminRepositoryImpl.REJECTED -> root.background.setTint(root.context.getColor(R.color.rejected))
        }

        tvApplicationId.text = root.context.getString(
            R.string.admin_application_form_id,
            applicationForm.applicationId
        )
        tvApplicatoinCreator.text =
            root.context.getString(R.string.admin_applicant_name, applicationForm.fullName)
        root.setOnClickListener {
            onClicked(applicationForm)
        }
    }
}