package com.wirajasa.wirajasabisnis.buyer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityBuyerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuyerActivity : AppCompatActivity() {

    private val binding: ActivityBuyerBinding by lazy {
        ActivityBuyerBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        val fragmentContainerView: NavHostFragment =
            supportFragmentManager.findFragmentById(binding.buyerNavhost.id) as NavHostFragment
        val navController: NavController = fragmentContainerView.navController
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.dashboard_screen))
        setupActionBarWithNavController(
            navController = navController,
            configuration = appBarConfiguration
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(binding.buyerNavhost.id)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}