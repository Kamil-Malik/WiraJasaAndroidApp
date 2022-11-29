package com.wirajasa.wirajasabisnis.feature_admin.ui.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.wirajasa.wirajasabisnis.data.model.SellerApplication
import com.wirajasa.wirajasabisnis.feature_admin.ui.activity.DetailFormActivity

class FormResultContract : ActivityResultContract<SellerApplication, Boolean>() {
    override fun createIntent(context: Context, input: SellerApplication): Intent {
        return Intent(context, DetailFormActivity::class.java).putExtra("FORM", input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}