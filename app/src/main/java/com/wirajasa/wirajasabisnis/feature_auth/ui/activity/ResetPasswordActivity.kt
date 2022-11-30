package com.wirajasa.wirajasabisnis.feature_auth.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityResetPasswordBinding
import com.wirajasa.wirajasabisnis.feature_auth.ui.viewmodel.ResetPasswordViewModel
import com.wirajasa.wirajasabisnis.feature_auth.domain.usecases.Validate
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel by viewModels<ResetPasswordViewModel>()
    private val binding by lazy {
        ActivityResetPasswordBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnReset.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val email = binding.edtEmail.text.toString()

        when (v?.id) {
            binding.btnReset.id -> {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(currentFocus?.windowToken, 0)

                if (!Validate().email(email)) {
                    binding.edtEmail.error = getString(R.string.tv_empty_invalid_email)
                    return
                }

                viewModel.resetPasswordWithEmailAddress(email).observe(this) {
                    when (it) {
                        is NetworkResponse.GenericException -> Snackbar.make(
                            binding.root, it.cause.toString(), Snackbar.LENGTH_LONG
                        ).show().also { showLoading(false) }
                        is NetworkResponse.Loading -> showLoading(true)
                        is NetworkResponse.Success -> Toast.makeText(
                            this,
                            getString(R.string.tv_reset_success),
                            Toast.LENGTH_LONG
                        ).show().also { finish() }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.apply {
            edtEmail.isEnabled = false
            btnReset.visibility = View.INVISIBLE
            pbLoading.visibility = View.VISIBLE
        } else binding.apply {
            edtEmail.isEnabled = true
            btnReset.visibility = View.VISIBLE
            pbLoading.visibility = View.GONE
        }
    }
}