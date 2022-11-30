package com.wirajasa.wirajasabisnis.presentation.service

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.databinding.ActivityUpdateServiceBinding
import com.wirajasa.wirajasabisnis.feature_seller.SellerBaseActivity
import com.wirajasa.wirajasabisnis.utility.Constant
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class UpdateServiceActivity : AppCompatActivity(), View.OnClickListener,
    EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityUpdateServiceBinding
    private var post: ServicePost? = null
    private var getUri: Uri? = null
    private val updateViewModel by viewModels<UpdatingServiceViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                DetailServiceActivity.EXTRA_SERVICE_POST,
                ServicePost::class.java
            )
        } else {
            intent.getParcelableExtra<ServicePost>(DetailServiceActivity.EXTRA_SERVICE_POST) as ServicePost
        }

        val arrayAdapter =
            ArrayAdapter(this, R.layout.textview, resources.getStringArray(R.array.province))
        binding.edtAddress.setText(post?.address)
        binding.edtService.setText(post?.name)
        binding.edtPrice.setText(post?.price.toString())
        binding.edtPhoneNumber.setText(post?.phoneNumber)
        binding.edtUnit.setText(post?.unit)
        binding.edtProvince.setText(post?.province)
        binding.edtProvince.setAdapter(arrayAdapter)
        if (post?.photoUrl != null) {
            binding.tvIcon.visibility = View.GONE
            binding.ivIcon.visibility = View.GONE
            Glide.with(this@UpdateServiceActivity)
                .load(post?.photoUrl)
                .into(binding.ivService)
        }
        binding.btnUpdateService.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_service -> startGallery()
            binding.btnUpdateService.id -> {
                val name = binding.edtService.text.toString()
                val price = binding.edtPrice.text.toString()
                val unit = binding.edtUnit.text.toString()
                val address = binding.edtAddress.text.toString()
                val province = binding.edtProvince.text.toString()
                val phoneNumber = binding.edtPhoneNumber.text.toString()
                val uid = post?.uid
                val serviceId = post?.serviceId

                if (name.isEmpty() || price.isEmpty() || unit.isEmpty()
                    || address.isEmpty() || province.isEmpty()
                    || phoneNumber.isEmpty() || (getUri == null || post?.photoUrl == null)
                ) {
                    Toast.makeText(
                        this@UpdateServiceActivity, getString(R.string.tv_form_have_to_be_filled),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    updateViewModel.updateProduct(
                        uid!!, serviceId!!, name, price.toInt(),
                        unit, address, province, phoneNumber, getUri
                    ).observe(this) {
                        when (it) {
                            is NetworkResponse.GenericException -> Toast.makeText(
                                this@UpdateServiceActivity,
                                it.cause.toString(),
                                Toast.LENGTH_SHORT
                            ).show().also { showLoading(false) }
                            is NetworkResponse.Loading -> showLoading(true)
                            is NetworkResponse.Success -> {
                                Toast.makeText(
                                    this@UpdateServiceActivity,
                                    getString(R.string.success_update_data),
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(
                                    this@UpdateServiceActivity,
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.apply {
            tvServiceRegister.visibility = View.INVISIBLE
            layoutService.visibility = View.INVISIBLE
            layoutAddress.visibility = View.INVISIBLE
            layoutProvince.visibility = View.INVISIBLE
            layoutPhoneNumber.visibility = View.INVISIBLE
            edtLayoutPrice.visibility = View.INVISIBLE
            edtLayoutUnit.visibility = View.INVISIBLE
            ivIcon.visibility = View.GONE
            tvIcon.visibility = View.GONE
            ivService.visibility = View.INVISIBLE
            tvLoading.visibility = View.VISIBLE
            pbUpdating.visibility = View.VISIBLE
            btnUpdateService.visibility = View.INVISIBLE
        } else binding.apply {
            tvServiceRegister.visibility = View.VISIBLE
            layoutService.visibility = View.VISIBLE
            layoutAddress.visibility = View.VISIBLE
            layoutPhoneNumber.visibility = View.VISIBLE
            edtLayoutPrice.visibility = View.VISIBLE
            edtLayoutUnit.visibility = View.VISIBLE
            layoutProvince.visibility = View.VISIBLE
            ivIcon.visibility = View.VISIBLE
            tvIcon.visibility = View.VISIBLE
            ivService.visibility = View.VISIBLE
            tvLoading.visibility = View.GONE
            pbUpdating.visibility = View.GONE
            btnUpdateService.visibility = View.VISIBLE
        }

    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = getString(R.string.image_type)
        val chooser = Intent.createChooser(intent, getString(R.string.tv_choose_a_picture))
        launcherIntentGallery.launch(chooser)
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
}