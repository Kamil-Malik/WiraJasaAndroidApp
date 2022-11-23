package com.wirajasa.wirajasabisnis.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityProfileBinding
import com.wirajasa.wirajasabisnis.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels()
    private val binding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarProfile)
        setContentView(binding.root)
        val profile = viewModel.getUser()
        binding.apply {
            if (profile.photourl.isNotBlank())
                Glide.with(this@ProfileActivity)
                    .load(profile.photourl)
                    .fitCenter()
                    .circleCrop()
                    .into(imgProfile)
            else
                Glide.with(this@ProfileActivity)
                    .load(R.drawable.default_image)
                    .fitCenter()
                    .circleCrop()
                    .into(imgProfile)

            tvUsername.text = profile.username
            tvEmail.text = Firebase.auth.currentUser?.email
            tvAddress.text = profile.address
            tvPhonenumber.text = profile.phone_number
            tvName.text = profile.uid
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> Firebase.auth.signOut().also {
                startActivity(
                    Intent(
                        this, LoginActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
        return true
    }
}