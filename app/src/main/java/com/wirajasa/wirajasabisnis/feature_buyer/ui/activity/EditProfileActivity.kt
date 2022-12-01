package com.wirajasa.wirajasabisnis.feature_buyer.ui.activity

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.usecases.CheckPermission
import com.wirajasa.wirajasabisnis.core.usecases.RequestPermission
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.databinding.ActivityEditProfileBinding
import com.wirajasa.wirajasabisnis.feature_buyer.ui.viewmodel.EditProfileViewModel
import com.wirajasa.wirajasabisnis.utility.Constant.READ_EXTERNAL
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity(), View.OnClickListener,
    EasyPermissions.PermissionCallbacks {

    private lateinit var profile: UserProfile
    private val checkPermission: CheckPermission = CheckPermission(this)
    private val requestPermission = RequestPermission(this)
    private val viewModel by viewModels<EditProfileViewModel>()
    private val binding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        profile = viewModel.getProfile()
        binding.apply {
            if (profile.image.isNotBlank()) Glide.with(this@EditProfileActivity).load(profile.image)
                .fitCenter().circleCrop().into(ivProfile)
            else Glide.with(this@EditProfileActivity).load(R.drawable.ic_profile_48).fitCenter()
                .circleCrop().into(ivProfile)

            edtUsername.setText(profile.username)
            edtAddress.setText(profile.address)
            edtPhonenumber.setText(profile.phone_number)

            binding.btnEditPhoto.setOnClickListener(this@EditProfileActivity)
            binding.btnSave.setOnClickListener(this@EditProfileActivity)
            binding.edtPhonenumber.addTextChangedListener {
                binding.layoutPhoneNumber.error?.let {
                    binding.layoutPhoneNumber.isErrorEnabled = false
                }
            }
        }
    }

    override fun onClick(v: View?) {
        val username = binding.edtUsername.text.toString()
        val phoneNumber = binding.edtPhonenumber.text.toString()
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
                if (userProfile.phone_number.first().toString() == "0") {
                    binding.layoutPhoneNumber.error =
                        getString(R.string.edtProfile_phone_number_starts_with_zero)
                    return
                }

                if (userProfile.phone_number.toLongOrNull() == null) {
                    binding.layoutPhoneNumber.error =
                        getString(R.string.edtProfile_phone_number_contains_invalid_characters)
                    return
                }

                if (userProfile.phone_number.length > 15) {
                    binding.layoutPhoneNumber.error =
                        getString(R.string.edtProfile_phone_number_exceed_limit)
                    return
                }

                userProfile.phone_number =
                    getString(R.string.edtProfile_template_phone_number, userProfile.phone_number)
                if (userProfile.address.isEmpty()) userProfile.address =
                    getString(R.string.not_setup)

                viewModel.updateProfile(userProfile).observe(this) { response ->
                    when (response) {
                        is NetworkResponse.GenericException -> {
                            isLoading(false)
                            response.cause?.let { shortMessage(it) }
                        }
                        is NetworkResponse.Loading -> {
                            response.status?.let { binding.tvLoading.text = it }
                            if (binding.pbLoading.visibility != View.VISIBLE) isLoading(true)
                        }
                        is NetworkResponse.Success -> {
                            Glide.with(this).load(viewModel.getProfile().image).fitCenter()
                                .circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(binding.ivProfile)

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
            pbLoading.visibility = View.VISIBLE
            tvLoading.visibility = View.VISIBLE
        } else binding.apply {
            btnSave.visibility = View.VISIBLE
            pbLoading.visibility = View.GONE
            tvLoading.visibility = View.GONE
        }
    }

    private val launcherIntentGallery: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            Log.d(ContentValues.TAG, " URI: $selectedImg")
            Glide.with(this).load(selectedImg).fitCenter().circleCrop().into(binding.ivProfile)
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

    private fun startGallery() {
        when {
            Build.VERSION.SDK_INT >= 33 -> {
                if (checkPermission.accessMedia()) openGallery()
                else requestPermission.accessMedia()
            }
            else -> {
                if (checkPermission.accessExternal()) openGallery()
                else requestPermission.accessExternal()
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