package com.wirajasa.wirajasabisnis.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityLoginBinding
import com.wirajasa.wirajasabisnis.presentation.main_activity.MainActivity
import com.wirajasa.wirajasabisnis.presentation.register_buyyer.RegisterActivity
import com.wirajasa.wirajasabisnis.presentation.reset_password.ResetPasswordActivity
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        window.statusBarColor = getColor(R.color.orange)
        supportActionBar?.hide()
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
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(currentFocus?.windowToken, 0)

                if (!validateEmail(email)) binding.edtEmail.error =
                    getString(R.string.empty_invalid_email)

                if (!validatePassword(password)) Snackbar.make(
                    binding.root, getString(R.string.empty_password), Snackbar.LENGTH_SHORT
                ).show().also { return }

                viewModel.signInWithEmailAndPassword(email, password).observe(this) {
                    when (it) {
                        is NetworkResponse.GenericException -> Snackbar.make(
                            binding.root, it.cause.toString(), Snackbar.LENGTH_LONG
                        ).show().also { showLoading(false) }
                        NetworkResponse.Loading -> showLoading(true)
                        is NetworkResponse.Success -> startActivity(Intent(
                            this, MainActivity::class.java
                        ).also { finish() })
                    }
                }
            }
            binding.btnRegister.id -> {
                startActivity(
                    Intent(
                        this, RegisterActivity::class.java
                    )
                )
            }
            binding.tvForgotPassword.id -> {
                startActivity(Intent(this, ResetPasswordActivity::class.java))
            }
        }
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

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) false
        else Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.isEmpty()) false
        else password.length >= 8
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}