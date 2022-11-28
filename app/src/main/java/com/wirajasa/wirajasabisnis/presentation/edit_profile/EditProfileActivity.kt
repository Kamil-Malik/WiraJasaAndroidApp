package com.wirajasa.wirajasabisnis.presentation.edit_profile

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.databinding.ActivityEditProfileBinding
import com.wirajasa.wirajasabisnis.utility.Constant.READ_EXTERNAL
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
            if (profile.image.isNotBlank()) Glide.with(this@EditProfileActivity)
                .load(profile.image)
                .fitCenter()
                .circleCrop()
                .into(imgProfile)
            else Glide.with(this@EditProfileActivity)
                .load(R.drawable.default_image)
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
            isSeller = profile.isSeller,
            isAdmin = profile.isAdmin
        )
        when (v?.id) {
            binding.btnEditPhoto.id -> startGallery()
            binding.btnSave.id -> {
                viewModel.updateProfile(userProfile).observe(this) { response ->
                    when (response) {
                        is NetworkResponse.GenericException -> {
                            isLoading(false)
                            response.cause?.let { shortMessage(it) }
                        }
                        is NetworkResponse.Loading -> {
                            response.status?.let { binding.tvLoading.text = it }
                            if (binding.circleLoading.visibility != View.VISIBLE) isLoading(true)
                        }
                        is NetworkResponse.Success -> {
                            Glide.with(this)
                                .load(viewModel.getProfile().image)
                                .fitCenter()
                                .circleCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(binding.imgProfile)

                            isLoading(false)
                            shortMessage("Profile Updated")
                            setResult(Activity.RESULT_OK)
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
            Log.d(ContentValues.TAG, " URI: $selectedImg")
            Glide.with(this).load(selectedImg)
                .fitCenter()
                .circleCrop()
                .into(binding.imgProfile)
            viewModel.setNewImageUri(selectedImg)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestMediaPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.tv_gallery_permission_title),
            READ_EXTERNAL,
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
            READ_EXTERNAL,
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
            READ_EXTERNAL -> openGallery()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            READ_EXTERNAL -> shortMessage(getString(R.string.tv_gallery_permission_denied))
        }
    }
}