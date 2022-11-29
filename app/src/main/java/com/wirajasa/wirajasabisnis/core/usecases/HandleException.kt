package com.wirajasa.wirajasabisnis.core.usecases

import android.content.Context
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.wirajasa.wirajasabisnis.R
import java.io.IOException

class HandleException(private val context: Context, private val e: Exception) {

    operator fun invoke(): String {
        return when (e) {
            is IOException -> context.getString(R.string.tv_connection_error)
            is FirebaseAuthEmailException -> context.getString(R.string.tv_invalid_email)
            is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.tv_invalid_credential)
            is FirebaseAuthInvalidUserException -> context.getString(R.string.tv_invalid_account)
            else -> context.getString(R.string.tv_something_went_wrong)
        }
    }
}