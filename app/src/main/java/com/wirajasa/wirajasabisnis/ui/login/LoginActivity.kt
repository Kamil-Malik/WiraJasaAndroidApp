package com.wirajasa.wirajasabisnis.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wirajasa.wirajasabisnis.databinding.ActivityLoginBinding
import com.wirajasa.wirajasabisnis.ui.main_activity.MainActivity
import com.wirajasa.wirajasabisnis.ui.register_buyyer.RegisterBuyyerActivity
import com.wirajasa.wirajasabisnis.ui.reset_password.ResetPasswordActivity
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
    }

    override fun onClick(v: View?) {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        when (v?.id) {
            binding.btnLogin.id -> viewModel.signInWithEmailAndPassword(
                email,
                password,
                Dispatchers.IO
            )
            binding.tvForgotPassword.id -> startActivity(
                Intent(this, ResetPasswordActivity::class.java)
            )
            binding.btnRegister.id -> startActivity(
                Intent(this, RegisterBuyyerActivity::class.java)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}