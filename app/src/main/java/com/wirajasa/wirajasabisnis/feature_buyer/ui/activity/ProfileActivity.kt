package com.wirajasa.wirajasabisnis.feature_buyer.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.ui.InformationActivity
import com.wirajasa.wirajasabisnis.databinding.ActivityProfileBinding
import com.wirajasa.wirajasabisnis.feature_auth.ui.activity.LoginActivity
import com.wirajasa.wirajasabisnis.feature_buyer.ui.viewmodel.ProfileViewModel
import com.wirajasa.wirajasabisnis.role_buyer.contract.EditProfileContract
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: ProfileViewModel by viewModels()
    private val binding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarProfile)
        setContentView(binding.root)
        setView()
        binding.apply {
            btnEdit.setOnClickListener(this@ProfileActivity)
            btnRegisterAsSeller.setOnClickListener(this@ProfileActivity)
            btnTermOfService.setOnClickListener(this@ProfileActivity)
            btnAboutUs.setOnClickListener(this@ProfileActivity)
        }
    }

    private fun setView() {
        val profile = viewModel.getUser()
        binding.apply {
            if (profile.image.isNotBlank()) Glide.with(this@ProfileActivity)
                .load(profile.image)
                .fitCenter()
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivProfile)
            else Glide.with(this@ProfileActivity).load(R.drawable.ic_profile_48).fitCenter()
                .circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(ivProfile)

            tvUsername.text = profile.username
            tvEmail.text = Firebase.auth.currentUser?.email
            tvAddress.text = profile.address
            tvPhonenumber.text = getString(R.string.profile_phone_number, profile.phone_number)
            tvName.text = profile.uid

            if (profile.isSeller) {
                btnRegisterAsSeller.visibility = View.GONE
            } else {
                btnRegisterAsSeller.visibility = View.VISIBLE
            }
        }
    }

    private val editProfileContract = registerForActivityResult(EditProfileContract()) { updated ->
        if (updated) setView()
    }

    private fun isDataFilled(): Boolean {
        val currentProfile = viewModel.getUser()

        if (!validateAddress(currentProfile.address)) {
            longMessage(getString(R.string.tv_please_fill_current_Profile))
            return false
        }

        if (!validateAddress(currentProfile.phone_number)) {
            longMessage(getString(R.string.tv_please_fill_current_Profile))
            return false
        }

        return true
    }

    private fun longMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun validateAddress(input: String): Boolean {
        if (input.isEmpty()) return false
        return (!input.matches(getString(R.string.tv_not_setup).toRegex()))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                Firebase.auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(
                    intent
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnAboutUs.id -> startActivity(Intent(this, InformationActivity::class.java))
            binding.btnEdit.id -> editProfileContract.launch(Unit)
            binding.btnRegisterAsSeller.id -> {
                if (isDataFilled()) {
                    val builder = AlertDialog.Builder(this@ProfileActivity, com.google.android.material.R.style.Theme_Material3_Light_Dialog_Alert)
                    builder.setTitle(R.string.alert_title)
                    builder.setMessage(R.string.alert_message)
                    builder.setIcon(R.drawable.ic_warning_24)
                    builder.setPositiveButton(R.string.yes){ dialog: DialogInterface, id : Int ->
                        startActivity(
                            Intent(
                                this,
                                UserValidation::class.java
                            )
                        )
                    }
                    builder.setNegativeButton(R.string.no){ dialog: DialogInterface, id: Int ->
                        dialog.cancel()
                    }

                    val dialog = builder.create()
                    dialog.show()
                }
            }
            binding.btnTermOfService.id -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(TERM_OF_SERVICE_URL))
                startActivity(browserIntent)
            }
        }
    }

    companion object{
        private const val TERM_OF_SERVICE_URL = "https://docs.google.com/document/d/1kKvvU00q73ssaCz9cK0HcYdlYff7Ya27/edit"
    }
}