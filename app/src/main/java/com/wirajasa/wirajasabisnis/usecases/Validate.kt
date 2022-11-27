package com.wirajasa.wirajasabisnis.usecases

import android.util.Patterns

class Validate {

    fun email(email: String) : Boolean {
        return if (email.isEmpty()) false
        else Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun password(password: String) : Boolean {
        return if (password.isEmpty()) false
        else password.length >= 8
    }
}