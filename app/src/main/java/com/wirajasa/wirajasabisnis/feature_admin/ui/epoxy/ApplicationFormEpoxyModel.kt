package com.wirajasa.wirajasabisnis.feature_admin.ui.epoxy

import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.epoxy.utility.ViewBindingKotlinModel
import com.wirajasa.wirajasabisnis.databinding.ItemRowApplicationFormBinding
import com.wirajasa.wirajasabisnis.utility.constant.ApplicationStatus.APPROVED
import com.wirajasa.wirajasabisnis.utility.constant.ApplicationStatus.PENDING
import com.wirajasa.wirajasabisnis.utility.constant.ApplicationStatus.REJECTED

data class ApplicationFormEpoxyModel(
    val applicationForm: SellerApplication,
    val onClicked: (SellerApplication) -> Unit
) : ViewBindingKotlinModel<ItemRowApplicationFormBinding>(R.layout.item_row_application_form) {

    override fun ItemRowApplicationFormBinding.bind() {
        when(applicationForm.applicationStatus) {
            PENDING -> root.background.setTint(root.context.getColor(R.color.pending))
            APPROVED -> root.background.setTint(root.context.getColor(R.color.approved))
            REJECTED -> root.background.setTint(root.context.getColor(R.color.rejected))
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