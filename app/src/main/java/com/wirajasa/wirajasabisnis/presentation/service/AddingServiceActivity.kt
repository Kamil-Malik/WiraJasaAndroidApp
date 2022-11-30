package com.wirajasa.wirajasabisnis.presentation.service

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.databinding.ActivityAddingServiceBinding
import com.wirajasa.wirajasabisnis.feature_seller.SellerBaseActivity
import com.wirajasa.wirajasabisnis.utility.Constant
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class AddingServiceActivity : AppCompatActivity(), View.OnClickListener,
    EasyPermissions.PermissionCallbacks {
    private var _binding: ActivityAddingServiceBinding? = null
    private val binding get() = _binding!!
    private var getUri: Uri? = null
    private val viewModel by viewModels<AddingServiceViewModel>()

    private lateinit var user: SellerApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddingServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = viewModel.getSellerProfile()
        binding.edtAddress.setText(user.address)
        binding.edtProvince.setText(user.province)
        binding.edtPhonenumber.setText(user.phoneNumber)
        binding.ivService.setOnClickListener(this)
        binding.btnAdd.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_service -> startGallery()
            binding.btnAdd.id -> {
                val name = binding.edtService.text.toString()
                val price = binding.edtPrice.text.toString()
                val unit = binding.edtUnit.text.toString()
                val address = binding.edtAddress.text.toString()
                val province = binding.edtProvince.text.toString()
                val phoneNumber = binding.edtPhonenumber.text.toString()
                val uid = user.uid

                if (name.isEmpty() || price.isEmpty() || unit.isEmpty()
                    || address.isEmpty() || province.isEmpty()
                    || phoneNumber.isEmpty() || getUri == null
                ) {
                    Toast.makeText(
                        this@AddingServiceActivity, getString(R.string.tv_form_have_to_be_filled),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.addProduct(
                        uid, name, price.toInt(), unit, address, province, phoneNumber, getUri
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
                                    this@AddingServiceActivity,
                                    getString(R.string.tv_success_add_data),
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

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = getString(R.string.image_type)
        val chooser = Intent.createChooser(intent, getString(R.string.tv_choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.apply {
            tvServiceRegister.visibility = View.INVISIBLE
            layoutService.visibility = View.INVISIBLE
            layoutAddress.visibility = View.INVISIBLE
            layoutProvince.visibility = View.INVISIBLE
            layoutPhonenumber.visibility = View.INVISIBLE
            edtLayoutPrice.visibility = View.INVISIBLE
            edtLayoutUnit.visibility = View.INVISIBLE
            ivIcon.visibility = View.GONE
            tvIcon.visibility = View.GONE
            ivService.visibility = View.INVISIBLE
            tvLoading.visibility = View.VISIBLE
            pbAdding.visibility = View.VISIBLE
            btnAdd.visibility = View.INVISIBLE
        } else binding.apply {
            tvServiceRegister.visibility = View.VISIBLE
            layoutService.visibility = View.VISIBLE
            layoutAddress.visibility = View.VISIBLE
            layoutPhonenumber.visibility = View.VISIBLE
            edtLayoutPrice.visibility = View.VISIBLE
            edtLayoutUnit.visibility = View.VISIBLE
            layoutProvince.visibility = View.VISIBLE
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
            Glide
                .with(this)
                .load(selectedImg)
                .fitCenter()
                .into(binding.ivService)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestMediaPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.tv_gallery_permission_title),
            Constant.READ_EXTERNAL,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkMediaPermission(): Boolean {
        return EasyPermissions
            .hasPermissions(this, Manifest.permission.READ_MEDIA_IMAGES)
    }

    private fun requestExternalAccessPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.tv_gallery_permission_title),
            Constant.READ_EXTERNAL,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun checkExternalAccessPermission(): Boolean {
        return EasyPermissions
            .hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun startGallery() {
        when {
            Build.VERSION.SDK_INT >= 33 -> {
                if (checkMediaPermission()) openGallery()
                else requestMediaPermission()
            }
            else -> {
                if (checkExternalAccessPermission()) openGallery()
                else requestExternalAccessPermission()
            }
        }
    }

    private fun shortMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            Constant.READ_EXTERNAL -> openGallery()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            Constant.READ_EXTERNAL -> shortMessage(getString(R.string.tv_gallery_permission_denied))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}