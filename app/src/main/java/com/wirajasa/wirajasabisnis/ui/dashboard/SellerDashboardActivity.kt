package com.wirajasa.wirajasabisnis.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wirajasa.wirajasabisnis.adapter.ProductAdapter
import com.wirajasa.wirajasabisnis.databinding.ActivitySellerDashboardBinding

class SellerDashboardActivity : AppCompatActivity() {

    private var _binding : ActivitySellerDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SellerDashboardViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySellerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(SellerDashboardViewModel::class.java)
        productAdapter = ProductAdapter()

        binding.rvListProduct.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(this@SellerDashboardActivity, LinearLayoutManager.VERTICAL, false)

        }
    }
}