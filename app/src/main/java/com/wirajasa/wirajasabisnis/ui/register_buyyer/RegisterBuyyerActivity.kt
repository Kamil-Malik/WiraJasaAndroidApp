package com.wirajasa.wirajasabisnis.ui.register_buyyer

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wirajasa.wirajasabisnis.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class RegisterBuyyerActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterBuyyerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnRegister.setOnClickListener(this@RegisterBuyyerActivity)
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