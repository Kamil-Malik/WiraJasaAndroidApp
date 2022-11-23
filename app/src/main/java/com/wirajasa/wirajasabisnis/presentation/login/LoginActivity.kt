package com.wirajasa.wirajasabisnis.presentation.login

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
import com.wirajasa.wirajasabisnis.databinding.ActivityLoginBinding
import com.wirajasa.wirajasabisnis.presentation.main_activity.MainActivity
import com.wirajasa.wirajasabisnis.presentation.register.RegisterActivity
import com.wirajasa.wirajasabisnis.presentation.reset_password.ResetPasswordActivity
import com.wirajasa.wirajasabisnis.ui.seller.SellerBaseActivity
import com.wirajasa.wirajasabisnis.usecases.Validate
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel by viewModels<LoginViewModel>()
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (viewModel.getCurrentUser() != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.apply {
            btnLogin.setOnClickListener(this@LoginActivity)
            btnRegister.setOnClickListener(this@LoginActivity)
            tvForgotPassword.setOnClickListener(this@LoginActivity)
        }
    }

    override fun onClick(v: View?) {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        when (v?.id) {
            binding.btnLogin.id -> {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    currentFocus?.windowToken, 0
                )

                if (!Validate().email(email)) binding.edtEmail.error =
                    getString(R.string.empty_invalid_email)

                if (!Validate().password(password)) Snackbar.make(
                    binding.root, getString(R.string.empty_password), Snackbar.LENGTH_SHORT
                ).show().also { return }

                viewModel.signInWithEmailAndPassword(email, password).observe(this) { response ->
                    when (response) {
                        is NetworkResponse.GenericException -> {
                            response.cause?.let { showToast(it) }
                            showLoading(false)
                        }
                        NetworkResponse.Loading -> showLoading(true)

                        is NetworkResponse.Success -> getProfile()
                    }
                }
            }
            binding.btnRegister.id -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
            binding.tvForgotPassword.id -> {
                val intent = Intent(this, ResetPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getProfile() {
        viewModel.getProfile().observe(this) { response ->
            when (response) {
                is NetworkResponse.GenericException -> {
                    response.cause?.let { showToast(it) }
                    showLoading(false)
                    Firebase.auth.signOut()
                    binding.tvLoading.text = getString(R.string.signing_in)
                }
                NetworkResponse.Loading -> binding.tvLoading.text =
                    getString(R.string.getting_profile)
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.apply {
            btnLogin.visibility = View.INVISIBLE
            tvOr.visibility = View.INVISIBLE
            btnRegister.visibility = View.INVISIBLE
            tvForgotPassword.visibility = View.INVISIBLE
            circleLoading.visibility = View.VISIBLE
            tvLoading.visibility = View.VISIBLE
        } else binding.apply {
            btnLogin.visibility = View.VISIBLE
            tvOr.visibility = View.VISIBLE
            btnRegister.visibility = View.VISIBLE
            tvForgotPassword.visibility = View.VISIBLE
            circleLoading.visibility = View.GONE
            tvLoading.visibility = View.GONE
        }
    }
}