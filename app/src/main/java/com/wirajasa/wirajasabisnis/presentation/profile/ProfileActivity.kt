package com.wirajasa.wirajasabisnis.presentation.profile

import android.content.Intent
import android.net.Uri
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
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.databinding.ActivityProfileBinding
import com.wirajasa.wirajasabisnis.presentation.edit_profile.EditProfileActivity
import com.wirajasa.wirajasabisnis.presentation.login.LoginActivity
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var profile: UserProfile
    private val viewModel: ProfileViewModel by viewModels()
    private val binding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarProfile)
        setContentView(binding.root)
        profile = viewModel.getUser()
        binding.apply {
            if (profile.image.isNotBlank()) Glide.with(this@ProfileActivity)
                .load(profile.image).fitCenter().circleCrop().into(imgProfile)
            else Glide.with(this@ProfileActivity).load(R.drawable.default_image).fitCenter()
                .circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imgProfile)

            tvUsername.text = profile.username
            tvEmail.text = Firebase.auth.currentUser?.email
            tvAddress.text = profile.address
            tvPhonenumber.text = profile.phone_number
            tvName.text = profile.uid

            if (profile.sellerStatus) {
                btnRegisterSeller.visibility = View.GONE
                spacerBottom.visibility = View.VISIBLE
            } else {
                btnRegisterSeller.visibility = View.VISIBLE
                spacerBottom.visibility = View.VISIBLE
            }

            btnEdit.setOnClickListener(this@ProfileActivity)
            btnRegisterSeller.setOnClickListener(this@ProfileActivity)
            fabUpload.setOnClickListener(this@ProfileActivity)
        }
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent.apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnEdit.id -> startActivity(Intent(this, EditProfileActivity::class.java))
            binding.fabUpload.id -> {
                viewModel.uploadImage(profile.uid).observe(this) { response ->
                    when (response) {
                        is NetworkResponse.GenericException -> {
                            isLoading(false)
                            response.cause?.let { shortMessage(it) }
                        }
                        NetworkResponse.Loading -> isLoading(true)
                        is NetworkResponse.Success -> {
                            Glide.with(this).load(response.data).fitCenter().circleCrop()
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .into(binding.imgProfile)

                            if (profile.image.isBlank() || Uri.parse(profile.image) != response.data) {
                                profile.image = response.data.toString()
                                updateProfile()
                            } else isLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun updateProfile() {
        viewModel.updateProfile(profile).observe(this) { response ->
            when (response) {
                is NetworkResponse.GenericException -> {
                    isLoading(false)
                    response.cause?.let { shortMessage(it) }
                }
                NetworkResponse.Loading -> binding.tvLoading.text =
                    getString(R.string.loading_status_updating_profile_data)
                is NetworkResponse.Success -> {
                    isLoading(false)
                    binding.tvLoading.text = getString(R.string.loading_status_uploading_image)
                    Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isLoading(loading: Boolean) {
        if (loading) {
            binding.fabUpload.visibility = View.INVISIBLE
            binding.circleLoading.visibility = View.VISIBLE
            binding.tvLoading.visibility = View.VISIBLE
        } else {
            binding.circleLoading.visibility = View.GONE
            binding.tvLoading.visibility = View.GONE
        }
    }

    private fun shortMessage(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}