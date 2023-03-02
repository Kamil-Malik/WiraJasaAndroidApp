package com.wirajasa.wirajasabisnis.role_buyer.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.wirajasa.wirajasabisnis.role_buyer.edit_profile.EditProfileActivity

class EditProfileContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, EditProfileActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}