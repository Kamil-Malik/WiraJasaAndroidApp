package com.wirajasa.wirajasabisnis.presentation.register_buyyer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityRegisterBinding
import com.wirajasa.wirajasabisnis.presentation.main_activity.MainActivity
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        window.statusBarColor = getColor(R.color.orange)
        supportActionBar?.hide()
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
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(currentFocus?.windowToken, 0)

                if (!validateEmail(email)) binding.edtEmail.error =
                    getString(R.string.empty_invalid_email)

                if (!validatePassword(password)) Snackbar.make(
                    binding.root, getString(R.string.empty_password), Snackbar.LENGTH_SHORT
                ).show().also { return }

                if (!validatePassword(confirmedPassword)) Snackbar.make(
                    binding.root,
                    getString(R.string.empty_confirmation_password),
                    Snackbar.LENGTH_SHORT
                ).show().also { return }

                if (password != confirmedPassword) Snackbar.make(
                    binding.root, getString(R.string.different_password), Snackbar.LENGTH_SHORT
                ).show().also { return }

                viewModel.signUpWithEmailAndPassword(email, password).observe(this) {
                    when (it) {
                        is NetworkResponse.GenericException -> Snackbar.make(
                            binding.root, it.cause.toString(), Snackbar.LENGTH_LONG
                        ).show().also { showLoading(false) }
                        NetworkResponse.Loading -> showLoading(true)
                        is NetworkResponse.Success -> registerDefaultUser()
                    }
                }
            }
        }
    }

    private fun registerDefaultUser() {
        viewModel.registerDefaultProfile().observe(this){
            when(it){
                is NetworkResponse.GenericException -> Unit
                NetworkResponse.Loading -> Unit
                is NetworkResponse.Success -> Toast.makeText(
                    this,
                    getString(R.string.welcome_user, it.data.username),
                    Toast.LENGTH_SHORT
                ).show().also {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.apply {
            btnRegister.visibility = View.INVISIBLE
            layoutClickLogin.visibility = View.INVISIBLE
            circleLoading.visibility = View.VISIBLE
        } else binding.apply {
            btnRegister.visibility = View.VISIBLE
            layoutClickLogin.visibility = View.VISIBLE
            circleLoading.visibility = View.VISIBLE
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