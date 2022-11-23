package com.wirajasa.wirajasabisnis.presentation.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityRegisterBinding
import com.wirajasa.wirajasabisnis.presentation.main_activity.MainActivity
import com.wirajasa.wirajasabisnis.usecases.Validate
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel by viewModels<RegisterViewModel>()
    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            btnRegister.setOnClickListener(this@RegisterActivity)
            tvClickHaveAccount.setOnClickListener(this@RegisterActivity)
        }
    }

    override fun onClick(v: View?) {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val confirmedPassword = binding.edtConfirmPassword.text.toString()
        when (v?.id) {
            binding.btnRegister.id -> {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    currentFocus?.windowToken, 0
                )

                if (!Validate().email(email)) binding.edtEmail.error =
                    getString(R.string.empty_invalid_email)

                if (!Validate().password(password)) {
                    showSnack(getString(R.string.empty_password))
                    return
                }

                if (!Validate().password(confirmedPassword)) {
                    showSnack(getString(R.string.empty_confirmation_password))
                    return
                }

                if (password != confirmedPassword) {
                    showSnack(getString(R.string.different_password))
                    return
                }

                viewModel.signUpWithEmailAndPassword(email, password).observe(this) { response ->
                    when (response) {
                        is NetworkResponse.GenericException -> {
                            response.cause?.let { showToast(it) }
                            showLoading(false)
                        }
                        NetworkResponse.Loading -> showLoading(true)
                        is NetworkResponse.Success -> registerDefaultUser()
                    }
                }
            }
            binding.tvClickHaveAccount.id -> finish()
        }
    }

    private fun registerDefaultUser() {
        viewModel.registerDefaultProfile().observe(this) { response ->
            when (response) {
                is NetworkResponse.GenericException -> {
                    response.cause?.let { showToast(it) }
                    Firebase.auth.signOut()
                    showToast("Sorry for inconvenience, please login again")
                    finish()
                }
                NetworkResponse.Loading -> binding.tvLoading.text =
                    getString(R.string.uploading_profile)
                is NetworkResponse.Success -> {
                    showToast(getString(R.string.welcome_user, response.data.username))
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSnack(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.apply {
            btnRegister.visibility = View.INVISIBLE
            layoutClickLogin.visibility = View.INVISIBLE
            circleLoading.visibility = View.VISIBLE
            tvLoading.visibility = View.VISIBLE
        } else binding.apply {
            btnRegister.visibility = View.VISIBLE
            layoutClickLogin.visibility = View.VISIBLE
            circleLoading.visibility = View.GONE
            tvLoading.visibility = View.GONE
        }
    }
}