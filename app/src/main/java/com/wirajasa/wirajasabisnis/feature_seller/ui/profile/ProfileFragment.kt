package com.wirajasa.wirajasabisnis.feature_seller.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.core.ui.InformationActivity
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.utility.constant.ApplicationStatus.REJECTED
import com.wirajasa.wirajasabisnis.databinding.FragmentProfileBinding
import com.wirajasa.wirajasabisnis.feature_auth.ui.activity.LoginActivity
import com.wirajasa.wirajasabisnis.feature_buyer.ui.activity.UserValidation
import com.wirajasa.wirajasabisnis.feature_seller.ui.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(), View.OnClickListener {

    private val viewModel by activityViewModels<ProfileViewModel>()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val userData = viewModel.getProfile()
        val sellerData = viewModel.getSellerProfile()

        Glide.with(this)
            .load(userData.image)
            .fitCenter()
            .circleCrop()
            .error(R.drawable.ic_profile_48)
            .into(binding.ivProfile)

        binding.apply {
            tvProfileName.text = sellerData.fullName
            tvProfileAddress.text = sellerData.address
            tvProfilePhoneNumber.text = sellerData.phoneNumber
            tvProfileVerificationStatus.text = getString(R.string.tv_application_status, sellerData.applicationStatus)
            btnLogOut.setOnClickListener(this@ProfileFragment)
            btnAboutUs.setOnClickListener(this@ProfileFragment)
            btnTermOfReference.setOnClickListener(this@ProfileFragment)
            if(sellerData.applicationStatus == REJECTED)
                btnEditValidation.visibility = View.VISIBLE
            btnEditValidation.setOnClickListener(this@ProfileFragment)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.btnLogOut.id -> {
                Firebase.auth.signOut()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(
                    intent
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            binding.btnTermOfReference.id -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/1061pzKw39f8D8TO-A7trCYjk113qb8W8m_yRS_QRXyI/edit?usp=sharing"))
                startActivity(browserIntent)
            }
            binding.btnAboutUs.id -> {
                startActivity(Intent(requireContext(), InformationActivity::class.java))
            }
            binding.btnEditValidation.id -> startActivity(Intent(requireContext(), UserValidation::class.java))
        }
    }
}