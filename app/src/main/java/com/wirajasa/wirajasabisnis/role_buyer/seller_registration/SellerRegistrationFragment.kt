package com.wirajasa.wirajasabisnis.role_buyer.seller_registration

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.usecases.CheckPermission
import com.wirajasa.wirajasabisnis.core.usecases.RequestPermission
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.core.utility.constant.Constant.READ_EXTERNAL
import com.wirajasa.wirajasabisnis.databinding.FragmentSellerRegistrationBinding
import com.wirajasa.wirajasabisnis.feature_auth.ui.activity.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class SellerRegistrationFragment : Fragment(R.layout.fragment_seller_registration),
    EasyPermissions.PermissionCallbacks,
    View.OnClickListener {

    private val viewModel: SellerRegistrationViewModel by viewModels()
    private val binding: FragmentSellerRegistrationBinding by viewBinding()
    private val requestPermission by lazy {
        RequestPermission(requireContext())
    }
    private val checkPermission by lazy {
        CheckPermission(requireContext())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val arrayAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.textview,
                resources.getStringArray(R.array.province)
            )

        val profile: UserProfile = viewModel.getCurrentUser()
        binding.edtFullName.setText(profile.username)
        binding.edtPhoneNumber.setText(profile.phone_number)
        binding.edtFullAddress.setText(profile.address)

        binding.edtProvince.setAdapter(arrayAdapter)
        binding.btnSelectFile.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            READ_EXTERNAL -> openGallery()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            READ_EXTERNAL -> Snackbar.make(
                binding.root,
                getString(R.string.tv_gallery_permission_denied),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onClick(v: View?) {
        val applicationForm = SellerApplication(
            uid = viewModel.getCurrentUser().uid,
            fullName = binding.edtFullName.text.toString(),
            address = binding.edtFullAddress.text.toString(),
            phoneNumber = binding.edtPhoneNumber.text.toString(),
            serviceDescription = binding.edtServiceDescription.text.toString(),
            province = binding.edtProvince.text.toString()
        )
        when (v?.id) {
            binding.btnSelectFile.id -> {
                startGallery()
            }

            binding.btnSubmit.id -> {
                if (applicationForm.province.isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.select_province),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return
                }

                if (viewModel.getImageUri().isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.btn_select_file),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return
                }

                uploadApplication(applicationForm)
            }
        }
    }

    private fun uploadApplication(form: SellerApplication) {
        viewModel.submitForm(form).observe(this) { response ->
            when (response) {
                is NetworkResponse.GenericException -> {
                    showLoading(false)
                    response.cause?.let {
                        Snackbar.make(
                            binding.root,
                            it,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                is NetworkResponse.Loading -> {
                    binding.tvLoading.text = response.status
                    showLoading(true)
                }

                is NetworkResponse.Success -> {
                    showLoading(false)
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    requireActivity().finishAfterTransition()
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
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
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
}