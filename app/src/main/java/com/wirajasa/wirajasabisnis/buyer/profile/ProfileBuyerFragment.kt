package com.wirajasa.wirajasabisnis.buyer.profile

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.R.style.Theme_Material3_Light_Dialog_Alert
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.databinding.FragmentProfileBuyerBinding
import com.wirajasa.wirajasabisnis.feature_auth.ui.activity.LoginActivity
import com.wirajasa.wirajasabisnis.feature_buyer.ui.activity.EditProfileContract
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileBuyerFragment : Fragment(), MenuProvider, View.OnClickListener {

    private var _binding: FragmentProfileBuyerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBuyerBinding.inflate(inflater, container, false)
        requireActivity().addMenuProvider(
            this,
            viewLifecycleOwner
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEdit.setOnClickListener(this)
        binding.btnRegisterAsSeller.setOnClickListener(this)
        binding.btnTermOfService.setOnClickListener(this)
        binding.btnAboutUs.setOnClickListener(this)
        setView()
    }

    private val editProfileContract: ActivityResultLauncher<Unit> =
        registerForActivityResult(EditProfileContract()) { updated ->
            if (updated) setView()
        }

    private fun setView() {
        val profile: UserProfile = viewModel.getUser()
        val isSeller = profile.isSeller
        val isNotBlank = profile.image.isNotBlank()
        if (isNotBlank) {
            Glide.with(requireContext())
                .load(profile.image)
                .centerInside()
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivProfile)
        } else {
            Glide
                .with(requireContext())
                .load(R.drawable.ic_profile_48)
                .centerInside()
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivProfile)
        }

        binding.tvUsername.text = profile.username
        binding.tvEmail.text = Firebase.auth.currentUser?.email
        binding.tvAddress.text = profile.address
        binding.tvPhonenumber.text = getString(R.string.profile_phone_number, profile.phone_number)
        binding.tvName.text = profile.uid

        if (isSeller) {
            binding.btnRegisterAsSeller.visibility = View.GONE
        } else {
            binding.btnRegisterAsSeller.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_logout -> {
                Firebase.auth.signOut()
                val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
                return true
            }
        }
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnEdit.id -> {
                editProfileContract.launch(Unit)
            }

            binding.btnRegisterAsSeller.id -> {
                val builder = AlertDialog.Builder(
                    requireContext(),
                    Theme_Material3_Light_Dialog_Alert
                )
                builder.setTitle(R.string.alert_title)
                builder.setMessage(R.string.alert_message)
                builder.setIcon(R.drawable.ic_warning_24)
                builder.setPositiveButton(R.string.yes) { _, _ ->
                   findNavController().navigate(
                       directions = ProfileBuyerFragmentDirections.profileToRegistration()
                   )
                }
                builder.setNegativeButton(R.string.no) { dialog: DialogInterface, _ ->
                    dialog.cancel()
                }

                val dialog = builder.create()
                dialog.show()
            }

            binding.btnAboutUs.id -> {
                findNavController().navigate(
                    directions = ProfileBuyerFragmentDirections.profileToWirajasaScreen()
                )
            }

            binding.btnTermOfService.id -> {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(TERM_OF_SERVICE_URL)
                )
                startActivity(browserIntent)
            }
        }
    }

    companion object {
        private const val TERM_OF_SERVICE_URL =
            "https://docs.google.com/document/d/1kKvvU00q73ssaCz9cK0HcYdlYff7Ya27/edit"
    }
}