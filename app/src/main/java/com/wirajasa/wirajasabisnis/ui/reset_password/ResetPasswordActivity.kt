package com.wirajasa.wirajasabisnis.ui.reset_password

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wirajasa.wirajasabisnis.databinding.ActivityResetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityResetPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ResetPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onClick(v: View?) {
        val email = binding.edtEmail.text.toString()
        when (v?.id) {
            binding.btnReset.id -> viewModel.sendResetPasswordLink(email, Dispatchers.IO)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}