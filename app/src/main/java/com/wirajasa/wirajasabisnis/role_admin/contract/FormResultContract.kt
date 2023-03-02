package com.wirajasa.wirajasabisnis.role_admin.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.role_admin.detail_form.DetailFormActivity
import com.wirajasa.wirajasabisnis.core.utility.constant.Dump.FORM

class FormResultContract : ActivityResultContract<SellerApplication, Boolean>() {
    override fun createIntent(context: Context, input: SellerApplication): Intent {
        return Intent(context, DetailFormActivity::class.java).putExtra(FORM, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}