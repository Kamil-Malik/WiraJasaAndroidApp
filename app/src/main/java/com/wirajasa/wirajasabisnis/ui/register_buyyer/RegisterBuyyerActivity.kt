package com.wirajasa.wirajasabisnis.ui.register_buyyer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wirajasa.wirajasabisnis.databinding.ActivityRegisterBuyyerBinding
import com.wirajasa.wirajasabisnis.ui.main_activity.MainActivity
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class RegisterBuyyerActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityRegisterBuyyerBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterBuyyerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBuyyerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnRegister.setOnClickListener(this@RegisterBuyyerActivity)
        }

        viewModel.registerStatus.observe(this) {
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
                    Toast.makeText(this, "Sukses Register", Toast.LENGTH_SHORT).show()
                    if (viewModel.getCurrentUser() != null)
                        startActivity(Intent(this, MainActivity::class.java))
                }
                else -> return@observe
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.emailError.collect(){
                binding.edtEmail.error = it
            }
        }
    }

    override fun onClick(v: View?) {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val confirmedPassword = binding.edtConfirmPassword.text.toString()
        when (v?.id) {
            binding.btnRegister.id -> {
                if (email.isNotEmpty() && (password == confirmedPassword)) viewModel
                    .signUpUserWithEmailAndPassword(
                        email,
                        password,
                        Dispatchers.IO
                    )
                else return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}