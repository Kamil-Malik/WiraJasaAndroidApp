package com.wirajasa.wirajasabisnis.feature_buyer.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityMainBinding
import com.wirajasa.wirajasabisnis.feature_buyer.ui.epoxy.ListOfServiceController
import com.wirajasa.wirajasabisnis.feature_buyer.ui.viewmodel.MainViewModel
import com.wirajasa.wirajasabisnis.presentation.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var controller: ListOfServiceController
    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarMain)
        setContentView(binding.root)
        viewModel.getAllService()

        controller = ListOfServiceController(onSelected = {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
        }, onRetry = {
            viewModel.getAllService()
        })

        binding.apply {
            epoxyService.setController(controller)
            epoxyService.layoutManager = LinearLayoutManager(this@MainActivity)
            epoxyService.setItemSpacingDp(8)
            epoxyService.setHasFixedSize(true)
        }
        subscribe()
    }

    private fun subscribe() {
        viewModel.listOfService.observe(this){
            controller.data = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_profile -> startActivity(Intent(this, ProfileActivity::class.java))
        }
        return true
    }
}