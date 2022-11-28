package com.wirajasa.wirajasabisnis.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityProfileBinding
import com.wirajasa.wirajasabisnis.presentation.edit_profile.EditProfileContract
import com.wirajasa.wirajasabisnis.presentation.login.LoginActivity
import com.wirajasa.wirajasabisnis.ui.user.UserValidation
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
            btnRegisterSeller.setOnClickListener(this@ProfileActivity)
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
                .into(imgProfile)
            else Glide.with(this@ProfileActivity).load(R.drawable.default_image).fitCenter()
                .circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imgProfile)

            tvUsername.text = profile.username
            tvEmail.text = Firebase.auth.currentUser?.email
            tvAddress.text = profile.address
            tvPhonenumber.text = profile.phone_number
            tvName.text = profile.uid

            if (profile.isSeller) {
                btnRegisterSeller.visibility = View.GONE
                spacerBottom.visibility = View.VISIBLE
            } else {
                btnRegisterSeller.visibility = View.VISIBLE
                spacerBottom.visibility = View.VISIBLE
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
            binding.btnEdit.id -> editProfileContract.launch(Unit)
            binding.btnRegisterSeller.id -> if (isDataFilled()) startActivity(
                Intent(
                    this,
                    UserValidation::class.java
                )
            )
        }
    }
}