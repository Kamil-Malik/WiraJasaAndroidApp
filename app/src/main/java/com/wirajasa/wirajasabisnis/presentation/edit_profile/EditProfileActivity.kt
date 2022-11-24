package com.wirajasa.wirajasabisnis.presentation.edit_profile

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.databinding.ActivityEditProfileBinding
import com.wirajasa.wirajasabisnis.utility.Constant
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity(), View.OnClickListener,
    EasyPermissions.PermissionCallbacks {

    private lateinit var profile: UserProfile
    private val viewModel by viewModels<EditProfileViewModel>()
    private val binding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        profile = viewModel.getProfile()
        binding.apply {
            Glide.with(this@EditProfileActivity)
                .load(profile.image)
                .fitCenter()
                .circleCrop()
                .into(imgProfile)
            edtUsername.setText(profile.username)
            edtAddress.setText(profile.address)
            edtPhoneNumber.setText(profile.phone_number)

            binding.btnEditPhoto.setOnClickListener(this@EditProfileActivity)
            binding.btnSave.setOnClickListener(this@EditProfileActivity)
        }
    }

    override fun onClick(v: View?) {
        val username = binding.edtUsername.text.toString()
        val phoneNumber = binding.edtPhoneNumber.text.toString()
        val address = binding.edtAddress.text.toString()
        val userProfile = UserProfile(
            username = username,
            uid = profile.uid,
            address = address,
            phone_number = phoneNumber,
            image = profile.image,
            sellerStatus = profile.sellerStatus,
            createdAt = profile.createdAt
        )
        when (v?.id) {
            binding.btnEditPhoto.id -> startGallery()
            binding.btnSave.id -> {
                if (viewModel.getProfile().image.isBlank() || viewModel.getProfile().image != profile.image) {
                    viewModel.updateProfileWithNewImage(userProfile).observe(this) { response ->
                        when (response) {
                            is NetworkResponse.GenericException -> {
                                isLoading(false)
                                response.cause?.let { shortMessage(it) }
                            }
                            NetworkResponse.Loading -> isLoading(true)
                            is NetworkResponse.Success -> {
                                isLoading(false)
                                shortMessage("Profile Updated")
                            }
                        }
                    }
                } else {
                    viewModel.updateProfileWithoutNewImage(userProfile).observe(this) { response ->
                        when (response) {
                            is NetworkResponse.GenericException -> {
                                isLoading(false)
                                response.cause?.let { shortMessage(it) }
                            }
                            NetworkResponse.Loading -> isLoading(true)
                            is NetworkResponse.Success -> {
                                isLoading(false)
                                shortMessage("Profile Updated")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) binding.apply {
            btnSave.visibility = View.INVISIBLE
            circleLoading.visibility = View.VISIBLE
            tvLoading.visibility = View.VISIBLE
        } else binding.apply {
            btnSave.visibility = View.VISIBLE
            circleLoading.visibility = View.GONE
            tvLoading.visibility = View.GONE
        }
    }

    private val launcherIntentGallery: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            Glide.with(this).load(selectedImg)
                .fitCenter()
                .circleCrop()
                .into(binding.imgProfile)
            viewModel.setNewImageUri(selectedImg)
        }
    }

    private fun startGallery() {
        when {
            Build.VERSION.SDK_INT >= 33 -> {
                if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                    launcherIntentGallery.launch(
                        Intent.createChooser(
                            Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                            getString(R.string.select_image)
                        )
                    )
                } else {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.gallery_permission_title),
                        Constant.READ_EXTERNAL,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                }
            }
            else -> {
                if (EasyPermissions.hasPermissions(
                        this, Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    launcherIntentGallery.launch(
                        Intent.createChooser(
                            Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                            getString(R.string.select_image)
                        )
                    )
                } else {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.gallery_permission_title),
                        Constant.READ_EXTERNAL,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            }
        }
    }

    private fun shortMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            Constant.READ_EXTERNAL -> startGallery()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            Constant.READ_EXTERNAL -> shortMessage(getString(R.string.gallery_permission_denied))
        }
    }
}