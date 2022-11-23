package com.wirajasa.wirajasabisnis.presentation.service

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityAddingServiceBinding
import com.wirajasa.wirajasabisnis.presentation.login.LoginActivity
import com.wirajasa.wirajasabisnis.presentation.login.LoginViewModel
import com.wirajasa.wirajasabisnis.ui.seller.SellerBaseActivity
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddingServiceActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding: ActivityAddingServiceBinding? = null
    private val binding get() = _binding!!
    private var getUri: Uri? = null

    private val viewModel by viewModels<AddingServiceViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddingServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        if (loginViewModel.getCurrentUser() == null) {
            val intent = Intent(this@AddingServiceActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        binding.ivService.setOnClickListener(this)
        binding.btnAdd.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivService -> startGallery()
            binding.btnAdd.id -> {
                val name = binding.edtService.text.toString()
                val price = binding.edtPrice.text.toString()
                val unit = binding.edtUnit.text.toString()
                val address = binding.edtAddress.text.toString()
                val email = binding.edtEmail.text.toString()
                val phoneNumber = binding.edtPhone.text.toString()
                val uid = loginViewModel.getCurrentUser()?.uid

                if (name.isEmpty() || price.isEmpty() || unit.isEmpty()
                    || address.isEmpty() || email.isEmpty()
                    || phoneNumber.isEmpty() || getUri == null
                ) {
                    Toast.makeText(
                        this@AddingServiceActivity, "Form have to be filled",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.addProduct(
                        uid!!, name, price.toInt(), unit, address, email, phoneNumber, getUri
                    ).observe(this) {
                        when (it) {
                            is NetworkResponse.GenericException -> Toast.makeText(
                                this@AddingServiceActivity,
                                it.cause.toString(),
                                Toast.LENGTH_SHORT
                            ).show().also { showLoading(false) }
                            is NetworkResponse.Loading -> showLoading(true)
                            is NetworkResponse.Success -> {
                                Toast.makeText(
                                    this@AddingServiceActivity, "Success",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(
                                    this@AddingServiceActivity,
                                    SellerBaseActivity::class.java
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.apply {
            textView.visibility = View.INVISIBLE
            edtLayoutService.visibility = View.INVISIBLE
            edtLayoutAddress.visibility = View.INVISIBLE
            edtLayoutEmail.visibility = View.INVISIBLE
            edtLayoutPhone.visibility = View.INVISIBLE
            edtLayoutPrice.visibility = View.INVISIBLE
            edtLayoutUnit.visibility = View.INVISIBLE
            ivIcon.visibility = View.GONE
            tvIcon.visibility = View.GONE
            ivService.visibility = View.INVISIBLE
            tvLoading.visibility = View.VISIBLE
            pbAdding.visibility = View.VISIBLE
            btnAdd.visibility = View.INVISIBLE
        } else binding.apply {
            textView.visibility = View.VISIBLE
            edtLayoutService.visibility = View.VISIBLE
            edtLayoutAddress.visibility = View.VISIBLE
            edtLayoutEmail.visibility = View.VISIBLE
            edtLayoutPhone.visibility = View.VISIBLE
            edtLayoutPrice.visibility = View.VISIBLE
            edtLayoutUnit.visibility = View.VISIBLE
            ivIcon.visibility = View.VISIBLE
            tvIcon.visibility = View.VISIBLE
            ivService.visibility = View.VISIBLE
            tvLoading.visibility = View.GONE
            pbAdding.visibility = View.GONE
        }

    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            getUri = selectedImg
            binding.ivIcon.visibility = View.GONE
            binding.tvIcon.visibility = View.GONE
            binding.ivService.setImageURI(selectedImg)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}