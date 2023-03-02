package com.wirajasa.wirajasabisnis.core.ui.detail_service

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.usecases.CurrencyFormatter
import com.wirajasa.wirajasabisnis.core.utility.constant.Constant
import com.wirajasa.wirajasabisnis.core.utility.constant.Dump
import com.wirajasa.wirajasabisnis.databinding.DetailServiceScreenBinding
import com.wirajasa.wirajasabisnis.feature_seller.ui.activity.UpdateServiceActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailServiceFragment : Fragment() {

    private var _binding: DetailServiceScreenBinding? = null
    private val binding get() = _binding!!
    private val args: DetailServiceFragmentArgs by navArgs()
    private val viewModel: DetailServiceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailServiceScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val localProfile: UserProfile = viewModel.getUser()
        val service: ServicePost = args.service
        val isSeller = localProfile.isSeller && localProfile.uid == service.uid
        if (isSeller) {
            binding.btnContactEdit.apply {
                background.setTint(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                text = getString(R.string.edit_service)
                setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit),
                    null
                )
            }
        }

        binding.tvPhoneNumber.text = getString(R.string.detail_service_phone_number, service.phoneNumber)
        binding.tvAddress.text = getString(R.string.detail_address, service.address, service.province)
        binding.tvPrice.text = CurrencyFormatter().invoke(service.price.toString())
        binding.tvServiceName.text = service.name
        binding.tvUnit.text = getString(R.string.detail_unit, service.unit)
        Glide.with(requireContext())
            .load(service.photoUrl)
            .into(binding.ivDetail)

        binding.btnContactWhatsapp.setOnClickListener {
            val rawLink = Dump.RAW_LINK + service.phoneNumber
            val link = Uri.parse(rawLink)
            startActivity(Intent(Intent.ACTION_VIEW).setData(link))
        }

        binding.btnContactEdit.setOnClickListener {
            if (isSeller) {
                val intent =
                    Intent(requireContext(), UpdateServiceActivity::class.java)
                intent.putExtra(Dump.EXTRA_SERVICE_POST, service)
                startActivity(intent)
            } else {
                val dialPhoneIntent =
                    Intent(Intent.ACTION_DIAL, Uri.parse(Constant.TEL + service.phoneNumber))
                startActivity(dialPhoneIntent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}