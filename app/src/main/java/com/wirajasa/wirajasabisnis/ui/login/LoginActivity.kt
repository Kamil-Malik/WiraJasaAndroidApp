package com.wirajasa.wirajasabisnis.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wirajasa.wirajasabisnis.databinding.ActivityLoginBinding
import com.wirajasa.wirajasabisnis.ui.main_activity.MainActivity
import com.wirajasa.wirajasabisnis.ui.reset_password.ResetPasswordActivity
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (viewModel.getCurrentUser() != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.apply {
            btnResetPassword.setOnClickListener(this@LoginActivity)
            btnLogin.setOnClickListener(this@LoginActivity)
        }

        viewModel.loginStatus.observe(this) {
            when (it) {
                is NetworkResponse.FirebaseException -> Toast.makeText(
                    this,
                    "${it.e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                NetworkResponse.Loading -> Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                    .show()
                NetworkResponse.NetworkException -> Toast.makeText(
                    this,
                    "Cek internet",
                    Toast.LENGTH_SHORT
                ).show()
                is NetworkResponse.Success -> {
                    Toast.makeText(this, "Sukses Login", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else -> return@observe
            }
        }
    }

    override fun onClick(v: View?) {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        when (v?.id) {
            binding.btnLogin.id -> viewModel.signInWithEmailAndPassword(email, password, Dispatchers.IO)
            binding.btnResetPassword.id -> startActivity(
                Intent(
                    this,
                    ResetPasswordActivity::class.java
                ).also { finish() }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}