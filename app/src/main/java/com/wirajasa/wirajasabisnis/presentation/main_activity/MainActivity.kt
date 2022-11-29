package com.wirajasa.wirajasabisnis.presentation.main_activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityMainBinding
import com.wirajasa.wirajasabisnis.presentation.profile.ProfileActivity
import com.wirajasa.wirajasabisnis.ui.dashboard.BuyerDashboardActivity

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbarMain)
        setContentView(binding.root)
        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, BuyerDashboardActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_profile -> startActivity(Intent(this, ProfileActivity::class.java))
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}