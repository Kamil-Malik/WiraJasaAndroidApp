package com.wirajasa.wirajasabisnis.ui.seller.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.FragmentProfileBinding
import com.wirajasa.wirajasabisnis.feature_auth.ui.activity.LoginActivity
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
            .error(R.drawable.default_image)
            .into(binding.ivProfile)

        binding.apply {
            tvProfileName.text = sellerData.fullName
            tvProfileAddress.text = sellerData.address
            tvProfilePhoneNumber.text = sellerData.phoneNumber
            tvProfileVerificationStatus.text = "Status : ${sellerData.applicationStatus}"
            btnLogout.setOnClickListener(this@ProfileFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.btnLogout.id -> {
                Firebase.auth.signOut()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(
                    intent
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }
}