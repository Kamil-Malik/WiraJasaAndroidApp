package com.wirajasa.wirajasabisnis.usecases

import android.content.Context
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.wirajasa.wirajasabisnis.R
import java.io.IOException

class HandleException {

    fun getMessage(exception: Exception, context: Context): String {
        return when (exception) {
            is IOException -> context.getString(R.string.connection_error)
            is FirebaseAuthEmailException -> context.getString(R.string.invalid_email)
            is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.invalid_credential)
            is FirebaseAuthInvalidUserException -> context.getString(R.string.invalid_account)
            else -> context.getString(R.string.something_went_wrong)
        }
    }
}