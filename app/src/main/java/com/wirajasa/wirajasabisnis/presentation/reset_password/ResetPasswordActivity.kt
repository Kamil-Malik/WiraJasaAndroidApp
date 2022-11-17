package com.wirajasa.wirajasabisnis.presentation.reset_password

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityResetPasswordBinding
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityResetPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ResetPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        window.statusBarColor = getColor(R.color.orange)
        supportActionBar?.hide()
        setContentView(binding.root)
        binding.btnReset.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val email = binding.edtEmail.text.toString()

        when (v?.id) {
            binding.btnReset.id -> {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(currentFocus?.windowToken, 0)

                if (!validateEmail(email)) {
                    binding.edtEmail.error = getString(R.string.empty_invalid_email)
                    return
                }

                viewModel.resetPasswordWithEmailAddress(email).observe(this) {
                    when (it) {
                        is NetworkResponse.GenericException -> Snackbar.make(
                            binding.root, it.cause.toString(), Snackbar.LENGTH_LONG
                        ).show().also { showLoading(false) }
                        NetworkResponse.Loading -> showLoading(true)
                        is NetworkResponse.Success -> Toast.makeText(
                            this,
                            getString(R.string.reset_success),
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
            circleLoading.visibility = View.VISIBLE
        } else binding.apply {
            edtEmail.isEnabled = true
            btnReset.visibility = View.VISIBLE
            circleLoading.visibility = View.GONE
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) false
        else Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}