package com.wirajasa.wirajasabisnis.feature_buyer.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.databinding.ActivityUserValidationBinding
import com.wirajasa.wirajasabisnis.core.usecases.CheckPermission
import com.wirajasa.wirajasabisnis.core.usecases.RequestPermission
import com.wirajasa.wirajasabisnis.core.utility.constant.Constant.READ_EXTERNAL
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.feature_auth.ui.activity.LoginActivity
import com.wirajasa.wirajasabisnis.feature_buyer.ui.viewmodel.UserValidationViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class UserValidation : AppCompatActivity(), View.OnClickListener,
    EasyPermissions.PermissionCallbacks {

    private val viewModel by viewModels<UserValidationViewModel>()
    private val requestPermission = RequestPermission(this)
    private val checkPermission = CheckPermission(this)
    private val binding by lazy {
        ActivityUserValidationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val arrayAdapter =
            ArrayAdapter(this, R.layout.textview, resources.getStringArray(R.array.province))
        val profile = viewModel.getCurrentUser()
        binding.apply {
            edtFullName.setText(profile.username)
            edtPhoneNumber.setText(profile.phone_number)
            edtFullAddress.setText(profile.address)

            edtProvince.setAdapter(arrayAdapter)
            btnSelectFile.setOnClickListener(this@UserValidation)
            btnSubmit.setOnClickListener(this@UserValidation)
        }
    }

    override fun onClick(v: View?) {
        val applicationForm = SellerApplication(
            uid = viewModel.getCurrentUser().uid,
            fullName = binding.edtFullName.text.toString(),
            address = binding.edtFullAddress.text.toString(),
            phoneNumber = binding.edtPhoneNumber.text.toString(),
            province = binding.edtProvince.text.toString()
        )
        when (v?.id) {
            binding.btnSubmit.id -> {
                if (applicationForm.province.isEmpty()) {
                    shortMessage(getString(R.string.select_province))
                    return
                }

                if (viewModel.getImageUri().isEmpty()) {
                    shortMessage(getString(R.string.btn_select_file))
                    return
                }

                uploadApplication(applicationForm)
            }
            binding.btnSelectFile.id -> startGallery()
        }
    }

    private fun uploadApplication(form: SellerApplication) {
        viewModel.submitForm(form).observe(this) { response ->
            when (response) {
                is NetworkResponse.GenericException -> {
                    showLoading(false)
                    response.cause?.let {
                        shortMessage(it)
                    }
                }
                is NetworkResponse.Loading -> {
                    response.status?.let {
                        binding.tvLoading.text = it
                    }
                    showLoading(true)
                }
                is NetworkResponse.Success -> {
                    showLoading(false)
                    val intent = Intent(this@UserValidation, LoginActivity::class.java)
                    startActivity(
                        intent
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    finish()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.apply {
            btnSubmit.visibility = View.INVISIBLE
            pbLoading.visibility = View.VISIBLE
            tvLoading.visibility = View.VISIBLE
        } else binding.apply {
            btnSubmit.visibility = View.VISIBLE
            pbLoading.visibility = View.GONE
            tvLoading.visibility = View.GONE
        }
    }

    private val launcherIntentGallery: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            binding.tvFileName.text = selectedImg.lastPathSegment
            viewModel.setImageUri(selectedImg)
        }
    }

    private fun openGallery() {
        launcherIntentGallery.launch(
            Intent.createChooser(
                Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                getString(R.string.tv_select_image)
            )
        )
    }

    private fun startGallery() {
        when {
            Build.VERSION.SDK_INT >= 33 -> {
                if (checkPermission.accessMedia()) {
                    openGallery()
                    return
                }
                requestPermission.accessMedia()
            }
            else -> {
                if (checkPermission.accessExternal()) {
                    openGallery()
                    return
                }
                requestPermission.accessExternal()
            }
        }
    }

    private fun shortMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            READ_EXTERNAL -> openGallery()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            READ_EXTERNAL -> shortMessage(getString(R.string.tv_gallery_permission_denied))
        }
    }
}